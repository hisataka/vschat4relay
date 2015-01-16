(ns vschat4relay.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [org.httpkit.client :as http])
  (:require [clojure.java.jdbc :as j])
  (:use [cheshire.core]))

; 状態管理atom
(def games (atom []))

; db関連
(load "db")

; http関連
(load "http")

; game関連
(load "game")

; ランダム文字列生成
(load "rnd")

;ルーティング設定
(defroutes app-routes
  (GET "/" [] "Hello World")
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
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
