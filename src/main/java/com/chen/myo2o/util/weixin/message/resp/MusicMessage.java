package com.chen.myo2o.util.weixin.message.resp;

/**
 * 音乐消息
 *
 * @author xiangli
 * @date 2015-02-10
 */
public class MusicMessage extends BaseMessage {
    // 音乐
    private com.chen.myo2o.util.weixin.message.resp.Music Music;

    public com.chen.myo2o.util.weixin.message.resp.Music getMusic() {
        return Music;
    }

    public void setMusic(com.chen.myo2o.util.weixin.message.resp.Music music) {
        Music = music;
    }
}