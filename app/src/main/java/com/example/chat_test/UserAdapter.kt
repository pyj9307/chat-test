package com.example.chat_test

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).
        inflate(R.layout.user_layout, parent, false)

        // RecyclerView형식의 UserViewHolder에 view를 넣고 뷰 반환
        return UserViewHolder(view)
    }

    // 뷰 내용 설정
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        // 데이터 담을 객체 생성
        val currentUser = userList[position]

        // nameText에 연결된 뷰에다가 데이터 담아주기
        holder.nameText.text = currentUser.name

        // itemView 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {

            val intent  = Intent(context, ChatActivity::class.java)

            //넘길 데이터
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uId", currentUser.uId)

            context.startActivity(intent)
        }
    }

    // 데이터 갯수 가져오기
    override fun getItemCount(): Int {
        return userList.size
    }

    // View 형식의 itemview를 RecyclerView형식으로 itemView 뿌리기
    //이 코드는 UserViewHolder 클래스를 정의하고 있습니다. UserViewHolder는 RecyclerView.ViewHolder 클래스를 상속받습니다.
    // itemView는 ViewHolder의 각 뷰 객체를 참조합니다. 따라서 itemView.findViewById(R.id.name_text)는
    // itemView에서 R.id.name_text에 해당하는 TextView를 찾아서 UserViewHolder 클래스 내의 nameText 
    // 멤버 변수에 할당합니다.
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        // id 가 name_text 인 뷰를 nameText 객체에 연결하기
        val nameText: TextView = itemView.findViewById(R.id.name_text)
    }
}