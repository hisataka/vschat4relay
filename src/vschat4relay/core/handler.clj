(ns vschat4relay.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [org.httpkit.client :as http])
  (:require [clojure.java.jdbc :as j]))

;;;;;;;;;; postgresql接続用
(def postgresql-db {:subprotocol "postgresql"
                    :subname "//ec2-174-129-1-179.compute-1.amazonaws.com:5432/d76k2v0lvos1l3?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
                    :user "ollzkkdgygzkti"
                    :password "3fdgK_t5FBW2n4-yGs5_D6Xh8f"})

;;;;;;;;;; ゲームの状態を管理するatom
(def is-started (atom false))
(def counter (atom 0))

;;;;;;;;;; 汎用
; jsonレスポンスを返す
(defn res-json [body]
  {:status 200 :headers {"Content-Type" "application/json; charset=UTF-8"} :body body})
; httpレスポンスを返す
(defn res-http [body]
  {:status 200 :headers {"Content-Type" "text/html; charset=UTF-8"} :body body})

;;;;;;;;;; テスト用
; チャットシステムエミュレート（常に応答はresmsg）
(defn make-chat-res [req]
  (str "{\"req\": \"" req "\", \"res\": \"resmsg\"}"))

;;;;;;;;;; 対話機能API
; チャット用rest-api呼び出し用（本文のJSON文字列を返す）
(defn rest-chat [url req]
  (:body
   @(http/get
    (str url "?req=" (java.net.URLEncoder/encode req "UTF-8")))))

;;;;;;;;;; ゲーム実行
; 会話ログをDBへ挿入
(defn regist-chat []
  (j/insert!
    postgresql-db
    :hoge
    {:col1 @counter}))


;;;;;;;;;; ルーティング設定
(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/chat" {params :params}
       (res-json
        (make-chat-res (params :req))))
  (GET "/is-started" []
       (res-http
        (if (true? @is-started) "true" "false")))
  (GET "/stop" []
       (do
         (reset! is-started false)
         (res-http
          (if (true? @is-started) "true" "false"))))
  (GET "/start" []
       (do
         (reset! is-started true)
         (res-http
          (if (true? @is-started) "true" "false"))))
  (GET "/counter" []
       (res-http (str @counter)))
  (GET "/inc-counter" []
       (do
         (swap! counter inc)
         (res-http (str @counter))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
