package androidkotlin.trainning

import android.os.Parcel
import android.os.Parcelable

data class Note(var title: String = "",
                var text: String = "",
                var filename: String = "") : Parcelable, java.io.Serializable{

        constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString()) {

        }

    override fun writeToParcel(parcel: Parcel, flags: Int){
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(filename)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note>{
        private const val serialVersionUid: Long = 4242424242
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}