import android.media.AudioDeviceInfo;
import com.andrerinas.openheadunit.sdl.SdlMicrophone;
public class SdlMicPolicyCheck {
  static void require(boolean v) { if (!v) throw new AssertionError(); }
  public static void main(String[] args) {
    SdlMicrophone m=SdlMicrophone.INSTANCE;
    require(SdlMicrophone.Mode.Companion.fromKey(null)==SdlMicrophone.Mode.PHONE);
    require(SdlMicrophone.Mode.Companion.fromKey("bluetooth")==SdlMicrophone.Mode.BLUETOOTH);
    require(SdlMicrophone.Mode.Companion.fromKey("invalid")==SdlMicrophone.Mode.PHONE);
    require(m.matches(SdlMicrophone.Mode.PHONE,AudioDeviceInfo.TYPE_BUILTIN_MIC));
    require(!m.matches(SdlMicrophone.Mode.PHONE,AudioDeviceInfo.TYPE_BLUETOOTH_SCO));
    require(!m.matches(SdlMicrophone.Mode.BLUETOOTH,AudioDeviceInfo.TYPE_BLUETOOTH_A2DP));
    require(m.matches(SdlMicrophone.Mode.BLUETOOTH,AudioDeviceInfo.TYPE_BLUETOOTH_SCO));
    require(m.matches(SdlMicrophone.Mode.BLUETOOTH,AudioDeviceInfo.TYPE_BLE_HEADSET));
    require(m.matches(SdlMicrophone.Mode.AUTO,AudioDeviceInfo.TYPE_WIRED_HEADSET));
    System.out.println("PASS 9 microphone policy checks: phone default, persisted keys, built-in input, Bluetooth SCO/BLE, A2DP excluded, automatic");
  }
}
