package pl.com.morgoth.studia.semV.TW.lab6;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class LoggingHandler implements Runnable, EventHandler {

	private final SocketChannel socket;
	private final ByteBuffer dst = ByteBuffer.allocate(5000);
	private final FileChannel fileChannel;
        private SelectionKey registerKey;
        
	public LoggingHandler(Selector selector, SocketChannel socket, FileChannel fileChannel) throws IOException {
		this.socket = socket;
		this.fileChannel = fileChannel;
		this.socket.configureBlocking(false);
		
                registerKey = this.socket.register(selector, SelectionKey.OP_READ);
		registerKey.attach(this);
	}

	@Override
	public void handleEvent() {
		run();
	}

	@Override
	public void run() {
		try {
			int readed, wrote;
			while ((readed = socket.read(dst)) >= -1) {
				if(readed>0){
                                    if(readed<dst.capacity()){
                                        dst.put(System.lineSeparator().getBytes());
                                    }
                                    dst.flip();
                                    wrote = fileChannel.write(dst);
				LogManager.getLogger(LoggingHandler.class).log(Level.INFO, "log handler reads {}, wrote {}", readed,
						wrote);
                                }else if(readed ==-1){
                                    closeConnection();
                                }
				
			}
		} catch (IOException e) {
			LogManager.getLogger(LoggingHandler.class).error("run", e);
		}
	}

    private void closeConnection() {
        registerKey.cancel();
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(LoggingHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            LogManager.getLogger(LoggingHandler.class).log(Level.DEBUG, "connection to {} closed",socket);
    }

}