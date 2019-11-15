package bonch.dev.networkdz.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Unit_photo :RealmObject(){
    @PrimaryKey
    var id: Long = 0

    var title: String? = null
}