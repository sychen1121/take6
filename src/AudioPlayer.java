import javax.sound.sampled.*;   

import java.io.*;   
import java.net.*;   

/**
 * 播放音樂(支援WAV, AIFF, AU)<BR>
 * 2011/10/09
 * 
 * @version 1
 * @author Ray(吉他手)
 *
 */
public class AudioPlayer{   
    private AudioInputStream currentSound;   

    private Clip clip;   

    private float gain; 
    private FloatControl gainControl;

    //控制聲道,-1.0f:只有左聲道, 0.0f:雙聲道,1.0f右聲道
    private float pan; 
    private FloatControl panControl;  

    //控制靜音 開/關
    private boolean mute;
    private BooleanControl muteControl;

    //播放次數,小於等於0:無限次播放,大於0:播放次數
    private int playCount;

    private DataLine.Info dlInfo;
    private Object loadReference;   
    private AudioFormat format;

    public AudioPlayer(){   
        AudioPlayerInit();
    }

    public void AudioPlayerInit(){
        currentSound = null; 
        clip = null; 
        gain = 0.5f;
        gainControl = null;  
        pan = 0.0f;
        panControl = null;
        mute = false;
        muteControl = null;
        playCount = 1;
        dlInfo = null;
    }

    /**
     * 設定播放次數,播放次數,小於等於0:無限次播放,大於0:播放次數
     * @param c
     */
    public void setPlayCount(int c){
        playCount = c;
    }

    /**
     * 指定路徑讀取音檔,使用目前物件放置的package當相對路徑root,null時不使用物件路徑為root
     * @param filePath
     * @param obj 目前物件放置的package路徑
     */
    public void loadAudio(String filePath, Object obj){   
        try {
            if(obj != null){
                loadAudio(obj.getClass().getResourceAsStream(filePath));
            }else{
                loadAudio(new File(filePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**  
     * 從遠端讀取音檔
     */   
    public void loadAudio(URL url) throws Exception{   
        loadReference = url;    
        currentSound = AudioSystem.getAudioInputStream(url);    
        finishLoadingAudio();    
    }   

    /**
     * 讀取本地端音檔
     * @param file
     * @throws Exception
     */
    public void loadAudio(File file) throws Exception{   
        loadReference = file;    
        currentSound = AudioSystem.getAudioInputStream(file);    
        finishLoadingAudio();    
    }   

    /**
     * 從串流讀取音檔
     * @param iStream
     * @throws Exception
     */
    public void loadAudio(InputStream iStream) throws Exception{   
        loadReference = iStream;    

        if (iStream instanceof AudioInputStream){   
            currentSound = (AudioInputStream)iStream;   
        } else {   
            currentSound = AudioSystem.getAudioInputStream(iStream);   
        }   
        finishLoadingAudio();    
    }   

    /**  
     * load完音檔後，進行播放設定
     */   
    protected void finishLoadingAudio() throws Exception {   

        format = currentSound.getFormat();   


        dlInfo = new DataLine.Info(Clip.class, format, ((int) currentSound.getFrameLength() * format.getFrameSize()));   

        clip = (Clip) AudioSystem.getLine(dlInfo);   

        clip.open(currentSound);   
        clip.addLineListener(   
                new LineListener() {   
                    public void update(LineEvent event) {   
                        if (event.getType().equals(LineEvent.Type.STOP)){   
                            //System.out.println("音樂播完"+playCount);

                            if (clip != null){   
                                clip.stop();   
                                if (clip.getFramePosition() == clip.getFrameLength()){   
                                    clip.setFramePosition(0);   
                                }   
                            }
                            
                            //判斷播放次數已用完，若無限次則不關閉
                            if(playCount > 0){
                                playCount--;
                                if(playCount == 0){
                                    close();
                                    return;
                                }
                            }
                            play();
                        }   
                    }   
                }   
        );   
    }

    /**
     * 播放音檔
     */
    public void play(){
        if(clip != null){
            clip.start();   
        }
    }   

    /**
     * 暫停播放音檔
     */
    public void pause(){   
        if(clip != null){
            clip.stop();   
        }
    }   

    /**
     * 停止播放音檔,且將音檔播放位置移回開始處
     */
    public void stop(){
        if(clip != null){
            clip.stop();   
            clip.setFramePosition(0); 
        }
    }   

    /**
     * 設定音量
     * @param dB 0~1,預設為0.5
     */
    public void setVolume(float dB){
        float tempB = floor_pow(dB,1);
        //System.out.println("目前音量+"+tempB);
        gain = tempB;   
        resetVolume();

    }

    /**
     * @param min 要無條件捨去的數字
     * @param Num 要捨去的位數
     * 
     */
    private float floor_pow(float min, int Num){
        float n = (float)Math.pow(10, Num);
        float tmp_Num = ((int)(min*n))/n;
        return tmp_Num ;
    }

    /**
     * 重設音量
     */
    protected void resetVolume(){
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        //double gain = .5D; // number between 0 and 1 (loudest)
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }   

    /**
     * 設定聲道,-1.0f:只有左聲道, 0.0f:雙聲道,1.0f右聲道
     * @param p
     */
    public void setPan(float p){   
        pan = p;   
        resetPan();   
    }   

    /**
     * 重設單雙道、雙聲道
     */
    protected void resetPan(){   
        panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);   
        panControl.setValue(this.pan);   
    }

    /**
     * 設定靜音狀態,true:靜音,false:不靜音
     * @param m
     */
    public void setMute(boolean m){
        mute  = m;
        resetMute();
    }

    /**
     * 重設靜音狀態
     *
     */
    protected void resetMute(){
        muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
        muteControl.setValue(mute);
    }

    /**
     * 
     * @return
     */
    public int getFramePosition(){   
        try {   
            return clip.getFramePosition();   
        } catch (Exception e) {   
            return -1;   
        }   
    }   

    /**
     * 取得音檔格式
     * @return
     */
    public AudioFormat getCurrentFormat(){   
        return format;   
    }   

    /**
     * 取得音檔的串流
     * @return
     */
    public AudioInputStream getAudioInputStream(){   
        try {   
            AudioInputStream aiStream;   


            if (loadReference == null){   
                return null;   
            } else if (loadReference instanceof URL) {   
                URL url = (URL)loadReference;   
                aiStream = AudioSystem.getAudioInputStream(url);   
            } else if (loadReference instanceof File) {   
                File file = (File)loadReference;   
                aiStream = AudioSystem.getAudioInputStream(file);   
            } else if (loadReference instanceof AudioInputStream){   
                AudioInputStream stream = (AudioInputStream)loadReference;   
                aiStream = AudioSystem.getAudioInputStream(stream.getFormat(), stream);   
                stream.reset();   
            } else {   

                InputStream inputStream = (InputStream)loadReference;   
                aiStream = AudioSystem.getAudioInputStream(inputStream);   
            }   

            return aiStream;   
        } catch (Exception e) {   
            e.printStackTrace();   
            return null;   
        }   
    }   


    /**
     * 目前音檔是否已存在
     * @return
     */
    public boolean isAudioLoaded(){   
        return loadReference!= null;   
    }   

    /**
     * 取得剪輯音檔
     * @return
     */
    public Clip getClip() {   
        return clip;   
    } 

    /**  
     * 關閉音檔
     */   
    public void close(){   
        try {   
            if (clip != null)   
                clip.close();   
            if (currentSound != null)   
                currentSound.close();   
            loadReference = null;   
        } catch (Exception e){   
            System.out.println("unloadAudio: " + e);   
            e.printStackTrace();   
        }   

        currentSound = null;   
        clip = null;   
        gainControl = null;   
        panControl = null;   
        dlInfo = null;   
        loadReference = null;
        muteControl = null;
    }    
} 