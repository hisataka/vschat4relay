(in-ns 'vschat4relay.core.handler)

; postgresql接続用
(def postgresql-db {:subprotocol "postgresql"
                    :subname "//ec2-174-129-1-179.compute-1.amazonaws.com:5432/d76k2v0lvos1l3?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
                    :user "ollzkkdgygzkti"
                    :password "3fdgK_t5FBW2n4-yGs5_D6Xh8f"})

; 会話ログをDBへ挿入
(defn regist-chat [val]
  (j/insert!
    postgresql-db
    :hoge
    {:col1 val}))

; 次のゲームID
;(defn get-next-id []
;  (apply :nextval (j/query postgresql-db
;           ["select nextval('get_game_id');"])))
