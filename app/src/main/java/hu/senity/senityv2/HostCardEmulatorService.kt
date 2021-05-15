package hu.senity.senityv2
import android.content.SharedPreferences
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import hu.senity.senityv2.MainActivity.KEY_ID2


class HostCardEmulatorService: HostApduService() {
    companion object {
        val TAG = "Host Card Emulator"
        val STATUS_SUCCESS = "9000"
        val STATUS_FAILED = "6F00"
        val CLA_NOT_SUPPORTED = "6E00"
        val INS_NOT_SUPPORTED = "6D00"
        val AID = "A0000001020304"
        val SELECT_INS = "A4"
        val DEFAULT_CLA = "00"
        val MIN_APDU_LENGTH = 12
    }
    override fun onDeactivated(reason: Int) {
        //TODO("not implemented") //To change body of created
        // functions use File | Settings | File Templates.
        //Log.d(TAG, "Deactivated: " + reason)
    }

    override fun processCommandApdu(commandApdu: ByteArray?,
                                    extras: Bundle?): ByteArray {
        //TODO("not implemented") To change body of created
        // functions use File | Settings | File Templates.
        if (commandApdu == null) {
            return Utils.hexStringToByteArray(STATUS_FAILED)
        }

        val hexCommandApdu = Utils.toHex(commandApdu)

        if (hexCommandApdu.length < MIN_APDU_LENGTH) {
            return Utils.hexStringToByteArray(STATUS_FAILED)
        }

        if (hexCommandApdu.substring(0, 2) != DEFAULT_CLA) {
            return Utils.hexStringToByteArray(CLA_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(2, 4) != SELECT_INS) {
            return Utils.hexStringToByteArray(INS_NOT_SUPPORTED)
        }

        if (hexCommandApdu.substring(10, 24) == AID)  {
            //val sharedPreferences: SharedPreferences = this.getSharedPreferences(MainActivity.KEY_ID2, MODE_PRIVATE)
            //val uid: String? = sharedPreferences.getString(KEY_ID2,"")
            val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREF_NAME, MODE_PRIVATE)
            val uid = sharedPreferences.getString(KEY_ID2, "")
            print("HostCardEmulation: Emualted UID: ");
            println(uid.toString());
            return Utils.hexStringToByteArray(uid.toString())
            //return Utils.hexStringToByteArray("29AB686E")

            //return Utils.hexStringToByteArray(STATUS_SUCCESS)
        } else {
            return Utils.hexStringToByteArray(STATUS_FAILED)
        }

    }
}