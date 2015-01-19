(in-ns 'vschat4relay.core.handler)

; postgresql接続用
(def postgresql-db {:subprotocol "postgresql"
                    :subname "//ec2-174-129-1-179.compute-1.amazonaws.com:5432/d76k2v0lvos1l3?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
                    :user "ollzkkdgygzkti"
                    :password "3fdgK_t5FBW2n4-yGs5_D6Xh8f"})

; 会話ログをDBへ挿入
; create table chat_log (
; id serial primary key
; , game_id varchar(128)
; , bot_id varchar(128)
; , word varchar(512)
; , picture_url varchar(128)
; , ins_time timestamp default CURRENT_TIMESTAMP
; )


(defn regist-chat [id turn word picture_url]
  (j/insert!
   postgresql-db
   :chat_log
    {:game_id id :bot_id turn :word word :picture_url picture_url}))



(defn get-log
  ([]
   (res-json (generate-string (j/query postgresql-db
           ["select * from chat_log order by ins_time desc"]))))
  ([id]
   (res-json (generate-string (j/query postgresql-db
           ["select * from chat_log where game_id = ? order by ins_time desc" id])))))

(defn delete-log
  ([]
   (do
     (j/execute!
      postgresql-db
      ["delete from chat_log"])
     (res-http (str "ログをテーブルをクリアしました"))
    ))
  ([id]
   (do
     (j/delete!
      postgresql-db
      :chat_log
      ["game_id = ?" id])
     (res-http (str "game_id=" id "のログをテーブルから削除しました"))
    )))

(defn get-old-games []
  (res-json (generate-string (j/query postgresql-db ["select game_id, count(game_id) as count, max(ins_time) as ins_time from chat_log group by game_id order by max(ins_time) desc"]))))
