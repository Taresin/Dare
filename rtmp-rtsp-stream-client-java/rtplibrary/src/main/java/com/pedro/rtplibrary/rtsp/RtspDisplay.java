package com.pedro.rtplibrary.rtsp;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.pedro.encoder.utils.CodecUtil;
import com.pedro.rtplibrary.base.DisplayBase;
import com.pedro.rtsp.rtsp.Protocol;
import com.pedro.rtsp.rtsp.RtspClient;
import com.pedro.rtsp.rtsp.VideoCodec;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import java.nio.ByteBuffer;

/**
 * More documentation see:
 * {@link com.pedro.rtplibrary.base.DisplayBase}
 *
 * Created by pedro on 9/08/17.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RtspDisplay extends DisplayBase {

  private RtspClient rtspClient;

  public RtspDisplay(Context context, boolean useOpengl, ConnectCheckerRtsp connectCheckerRtsp) {
    super(context, useOpengl);
    rtspClient = new RtspClient(connectCheckerRtsp);
  }

  /**
   * Internet protocol used.
   *
   * @param protocol Could be Protocol.TCP or Protocol.UDP.
   */
  public void setProtocol(Protocol protocol) {
    rtspClient.setProtocol(protocol);
  }

  @Override
  public void resizeCache(int newSize) throws RuntimeException {
    rtspClient.resizeCache(newSize);
  }

  @Override
  public int getCacheSize() {
    return rtspClient.getCacheSize();
  }

  @Override
  public long getSentAudioFrames() {
    return rtspClient.getSentAudioFrames();
  }

  @Override
  public long getSentVideoFrames() {
    return rtspClient.getSentVideoFrames();
  }

  @Override
  public long getDroppedAudioFrames() {
    return rtspClient.getDroppedAudioFrames();
  }

  @Override
  public long getDroppedVideoFrames() {
    return rtspClient.getDroppedVideoFrames();
  }

  @Override
  public void resetSentAudioFrames() {
    rtspClient.resetSentAudioFrames();
  }

  @Override
  public void resetSentVideoFrames() {
    rtspClient.resetSentVideoFrames();
  }

  @Override
  public void resetDroppedAudioFrames() {
    rtspClient.resetDroppedAudioFrames();
  }

  @Override
  public void resetDroppedVideoFrames() {
    rtspClient.resetDroppedVideoFrames();
  }

  public void setVideoCodec(VideoCodec videoCodec) {
    recordController.setVideoMime(
        videoCodec == VideoCodec.H265 ? CodecUtil.H265_MIME : CodecUtil.H264_MIME);
    videoEncoder.setType(videoCodec == VideoCodec.H265 ? CodecUtil.H265_MIME : CodecUtil.H264_MIME);
  }

  @Override
  public void setAuthorization(String user, String password) {
    rtspClient.setAuthorization(user, password);
  }

  @Override
  protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
    rtspClient.setIsStereo(isStereo);
    rtspClient.setSampleRate(sampleRate);
  }

  @Override
  protected void startStreamRtp(String url) {
    rtspClient.connect(url);
  }

  @Override
  protected void stopStreamRtp() {
    rtspClient.disconnect();
  }

  @Override
  public void setReTries(int reTries) {
    rtspClient.setReTries(reTries);
  }

  @Override
  protected boolean shouldRetry(String reason) {
    return rtspClient.shouldRetry(reason);
  }

  @Override
  public void reConnect(long delay, @Nullable String backupUrl) {
    rtspClient.reConnect(delay, backupUrl);
  }

  @Override
  public boolean hasCongestion() {
    return rtspClient.hasCongestion();
  }

  @Override
  protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
    rtspClient.sendAudio(aacBuffer, info);
  }

  @Override
  protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
    ByteBuffer newSps = sps.duplicate();
    ByteBuffer newPps = pps.duplicate();
    ByteBuffer newVps = vps != null ? vps.duplicate() : null;
    rtspClient.setSPSandPPS(newSps, newPps, newVps);
  }

  @Override
  protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
    rtspClient.sendVideo(h264Buffer, info);
  }

  @Override
  public void setLogs(boolean enable) {
    rtspClient.setLogs(enable);
  }
}