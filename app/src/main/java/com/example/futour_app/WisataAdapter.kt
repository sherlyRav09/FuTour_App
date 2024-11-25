package com.example.futour_app


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction


class WisataAdapter(
    private val coffeeItems: ArrayList<WisataItem>,
    private val context: Context
) : RecyclerView.Adapter<WisataAdapter.ViewHolder>() {

    private lateinit var favDB: FavDB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        favDB = FavDB(context)

        // Create table on first start
        val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            createTableOnFirstStart()
        }

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coffeeItem = coffeeItems[position]
        readCursorData(coffeeItem, holder)
        holder.imageView.setImageResource(coffeeItem.imageResource)
        holder.titleTextView.text = coffeeItem.title
    }

    override fun getItemCount(): Int = coffeeItems.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleTextView: TextView = itemView.findViewById(R.id.namePlace)
        val likeCountTextView: TextView = itemView.findViewById(R.id.favDescrip)
        val favBtn: Button = itemView.findViewById(R.id.btnFavorite)

        init {
            // Add to fav button click listener
            favBtn.setOnClickListener {
                val position = adapterPosition
                val coffeeItem = coffeeItems[position]
                likeClick(coffeeItem, favBtn, likeCountTextView)
            }
        }
    }

    private fun createTableOnFirstStart() {
        favDB.insertEmpty()

        val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    @SuppressLint("Range")
    private fun readCursorData(wisataItem: WisataItem, viewHolder: ViewHolder) {
        val cursor: Cursor = favDB.readAllData(wisataItem.keyId)
        val db: SQLiteDatabase = favDB.readableDatabase
        try {
            while (cursor.moveToNext()) {
                val itemFavStatus = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS))
                wisataItem.favStatus = itemFavStatus

                // Check fav status
                if (itemFavStatus == "1") {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.baseline_favorite_red_24)
                } else {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.baseline_favorite_shadow_24)
                }
            }
        } finally {
            cursor.close()
            db.close()
        }
    }

    private fun likeClick(wisataItem: WisataItem, favBtn: Button, textLike: TextView) {
        val refLike: DatabaseReference = FirebaseDatabase.getInstance().getReference("likes")
        val upvotesRefLike: DatabaseReference = refLike.child(wisataItem.keyId)

        if (wisataItem.favStatus == "0") {
            wisataItem.favStatus = "1"
            favDB.insertIntoTheDatabase(wisataItem.title, wisataItem.imageResource, wisataItem.keyId, wisataItem.favStatus)
            favBtn.setBackgroundResource(R.drawable.baseline_favorite_red_24)
            favBtn.isSelected = true

            upvotesRefLike.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    try {
                        val currentValue = mutableData.getValue(Int::class.java)
                        if (currentValue == null) {
                            mutableData.value = 1
                        } else {
                            mutableData.value = currentValue + 1
                            Handler(Looper.getMainLooper()).post {
                                textLike.text = mutableData.value.toString()
                            }
                        }
                    } catch (e: Exception) {
                        throw e
                    }
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                    println("Transaction completed")
                }
            })
        } else if (wisataItem.favStatus == "1") {
            wisataItem.favStatus = "0"
            favDB.removeFav(wisataItem.keyId)
            favBtn.setBackgroundResource(R.drawable.baseline_favorite_shadow_24)
            favBtn.isSelected = false

            upvotesRefLike.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    try {
                        val currentValue = mutableData.getValue(Int::class.java)
                        if (currentValue == null) {
                            mutableData.value = 1
                        } else {
                            mutableData.value = currentValue - 1
                            Handler(Looper.getMainLooper()).post {
                                textLike.text = mutableData.value.toString()
                            }
                        }
                    } catch (e: Exception) {
                        throw e
                    }
                    return Transaction.success(mutableData)
                }

                override fun onComplete(databaseError: DatabaseError?, b: Boolean, dataSnapshot: DataSnapshot?) {
                    println("Transaction completed")
                }
            })
        }
    }
}
