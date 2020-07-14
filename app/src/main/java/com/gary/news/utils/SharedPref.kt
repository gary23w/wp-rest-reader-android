package com.gary.news.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.lang.reflect.Modifier
import java.util.*
import kotlin.collections.ArrayList


class SharedPref(context: Context) {
    val saveImage = ""
    var prefs: SharedPreferences = context.getSharedPreferences("IMAGE", Context.MODE_PRIVATE);
    var savePref: String?
        get() = prefs.getString(saveImage, saveImage)
        set(value) = prefs.edit().putString(saveImage, value).apply()
    //shared array//
    class MyStorage {
        fun storeFavorites(
            context: Context,
            favorites: List<*>?
        ) {
            // used for store arrayList in json format
            val settings: SharedPreferences
            val editor: SharedPreferences.Editor
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            val builder = GsonBuilder()
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            builder.excludeFieldsWithoutExposeAnnotation()
            val sExposeGson = builder.create()
            val jsonFavorites = sExposeGson.toJson(favorites)
            editor.putString(FAVORITES, jsonFavorites)
            editor.commit()
        }
        fun loadFavorites(context: Context): List<*>? {
            // used for retrieving arraylist from json formatted string
            val settings: SharedPreferences
            var favorites: List<*>
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            if (settings.contains(FAVORITES)) {
                val jsonFavorites =
                    settings.getString(FAVORITES, null)
                val builder = GsonBuilder()
                builder.excludeFieldsWithModifiers(
                    Modifier.FINAL,
                    Modifier.TRANSIENT,
                    Modifier.STATIC
                )
                builder.excludeFieldsWithoutExposeAnnotation()
                val sExposeGson = builder.create()
                val favoriteItems: Array<MyModelClass> =
                    sExposeGson.fromJson<Array<MyModelClass>>(
                        jsonFavorites,
                        Array<MyModelClass>::class.java
                    )
                favorites = Arrays.asList(favoriteItems)
                favorites = ArrayList(favorites)
            } else return null
            return favorites
        }
        fun addFavorite(context: Context, myModel: MyModelClass?) {
            var favorites: MutableList<String>? = loadFavorites(context) as MutableList<String>?
            if (favorites == null) favorites = ArrayList<String>()
            favorites!!.add(myModel.toString())
            storeFavorites(context, favorites)
        }
        fun removeFavorite(context: Context, myModel: MyModelClass?) {
            val favorites: MutableList<String>? = loadFavorites(context) as MutableList<String>?
            if (favorites != null) {
                favorites.remove(myModel.toString())
                storeFavorites(context, favorites)
            }
        }
        companion object {
            const val PREFS_NAME = "MY_APP"
            const val FAVORITES = "Favorite"
        }
    }
    class MyModelClass : Serializable {
        //@Expose annotation used for Gson
        @Expose
        private val id: String? = null

        @Expose
        private val name: String? = null
    }


}


