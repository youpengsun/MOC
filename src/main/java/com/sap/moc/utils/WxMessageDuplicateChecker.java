package com.sap.moc.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

@Service
public class WxMessageDuplicateChecker implements IWxMessageDuplicateChecker {

	/**
	 * 一个消息ID在内存的过期时间：15秒
	 */
	private final Long timeToLive;

	/**
	 * 每隔多少周期检查消息ID是否过期：5秒
	 */
	private final Long clearPeriod;

	/**
	 * 消息id->消息时间戳的map
	 */
	private final ConcurrentHashMap<String, Long> msgId2Timestamp = new ConcurrentHashMap<String, Long>();

	/**
	 * 后台清理线程是否已经开启
	 */
	private final AtomicBoolean backgroundProcessStarted = new AtomicBoolean(false);

	/**
	   * WxMsgIdInMemoryDuplicateChecker构造函数
	   * <pre>
	   * 一个消息ID在内存的过期时间：15秒
	   * 每隔多少周期检查消息ID是否过期：5秒
	   * </pre>
	   */
	  public WxMessageDuplicateChecker() {
	    this.timeToLive = 15 * 1000l;
	    this.clearPeriod = 5 * 1000l;
	  }

	/**
	   * WxMessageDuplicateChecker Constructor
	   * @param timeToLive 一个消息ID在内存的过期时间：毫秒
	   * @param clearPeriod 每隔多少周期检查消息ID是否过期：毫秒
	   */
	  public WxMessageDuplicateChecker(Long timeToLive, Long clearPeriod) {
	    this.timeToLive = timeToLive;
	    this.clearPeriod = clearPeriod;
	  }

	protected void checkBackgroundProcessStarted() {
		if (backgroundProcessStarted.getAndSet(true)) {
			return;
		}
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(clearPeriod);
						Long now = System.currentTimeMillis();
						for (Map.Entry<String, Long> entry : msgId2Timestamp.entrySet()) {
							if (now - entry.getValue() > timeToLive) {
								msgId2Timestamp.entrySet().remove(entry);
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

	@Override
	public boolean isDuplicate(String messageId) {
		if (messageId == null) {
			return false;
		}
		checkBackgroundProcessStarted();
		Long timestamp = msgId2Timestamp.putIfAbsent(messageId, System.currentTimeMillis());
		if (timestamp == null) {
			// 第一次接收到这个消息
			return false;
		}
		return true;
	}

}
