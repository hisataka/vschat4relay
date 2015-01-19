(ns vschat4relay.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [org.httpkit.client :as http])
  (:require [clojure.java.jdbc :as j])
  (:use [cheshire.core]))

; 状態管理atom
(def games (atom []))

; http関連
(load "http")

; db関連
(load "db")

; game関連
(load "game")

; ランダム文字列生成
(load "rnd")

;ルーティング設定
(defroutes app-routes
  (GET "/" [] "running!")
  (GET "/start" {params :params}
         (start (params :bot_id1) (params :bot_id2) (params :start) (params :goal) (rand-str 30)))
  (GET "/stop" {params :params}
       (stop (params :game_id)))
  (GET "/botlist" []
       (bot-list))
  (GET "/gamelist" []
       (game-list))
  (GET "/chat" {params :params}
       (chat (params :game_id) (params :word)))
  (GET "/log" {params :params}
       (get-log (params :game_id)))
  (GET "/logall" []
       (get-log))
  (GET "/delete" {params :params}
       (delete-log (params :game_id)))
  (GET "/deleteall" []
       (delete-log))
  (route/files "/")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
