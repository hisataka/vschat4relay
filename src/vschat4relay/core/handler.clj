(ns vschat4relay.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [org.httpkit.client :as http])
  (:require [clojure.java.jdbc :as j]))

; 状態管理atom
(def games (atom []))

; db関連
(load "db")

; http関連
(load "http")

; game関連
(load "game")

; 対話機能関連
(load "chat")

; チャットシステムエミュレート
(defn make-chat-res [bot_id keyword]
  (str "{\"bot_id\": \"" bot_id "\", \"bot_name\": \"mybot\"" ", \"answer\" : \"hahaha\", \"picture_url\" : \"http://xxx.png\"}"))

;ルーティング設定
(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/bot/reaction" {params :params}
       (res-json
        (make-chat-res (params :bot_id) (params :keyword))))
  (GET "/start" {params :params}
         (start (params :id)))
  (GET "/stop" {params :params} (stop (params :id)))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
