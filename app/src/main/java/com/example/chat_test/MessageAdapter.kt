package com.example.chat_test

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val receive = 1 //받는 타입
    private val send = 2 //보내는 타입

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == 1){ //받는 화면
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        }else{ //보내는 화면
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            SendViewHolder(view)
        }
    }

    // 뷰 내용 설정
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //현재 메시지
        val currentMessage = messageList[position]

        //보내는 데이터
        if(holder.javaClass == SendViewHolder::class.java){
            val viewHolder = holder as SendViewHolder
            viewHolder.sendMessage.text = currentMessage.message
        }else{
            //받는 데이터
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // 메세지 뷰 타입 설정
    override fun getItemViewType(position: Int): Int {

        // currentMessage에는 message, sendId가 들어있음.
        val currentMessage = messageList[position]

        // 이 앱에 로그인 된 사용자(currentUser)의 Uid와 currentMessage에 담겨있는 sendId(Uid)가 같으면 send 반환
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)){
//            Log.d("lsy","FirebaseAuth.getInstance().currentUser?.uid :${FirebaseAuth.getInstance().currentUser?.uid}")
            send
            // 그 외 receive 반환
        }else{
            receive
        }
    }

    //보낸 쪽 뷰 연결
    class SendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sendMessage: TextView = itemView.findViewById(R.id.send_message_text)
    }

    //받는 쪽 뷰 연결
    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_message_text)
    }
}