/**
 * 
 */
package com.nio.util;

import java.nio.channels.FileChannel;

/**
 * @author Kolesnichenko
 */
public class FileChannelWrapper {
    
	private long size;
    private FileChannel fileChannel;
    private long position;
    
    public FileChannelWrapper(long size, FileChannel fileChannel) {
        this.setSize(size);
        this.setFileChannel(fileChannel);
        this.setPosition(0);
    }

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public FileChannel getFileChannel() {
		return fileChannel;
	}

	public void setFileChannel(FileChannel fileChannel) {
		this.fileChannel = fileChannel;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}
    
    public void incrementPosition(long value) {
        this.position += value;
    }
}
