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
  (GET "/" []
       (res-http (str
                  "<h1>vschat4relay</h1>"
                  "<h2>how to use?</h2>"
                  "<h3>/botlist</h3>"
                  "<p>see bot list!</p>"
                  "<h3>/start?bot_id1=xx&bot_id2=xx&start=xx&goal=xx</h3>"
                  "<p>new game start!</p>"
                  "<h3>/stop?game_id=xx</h3>"
                  "<p>game stop!</p>"
                  "<h3>/gamelist</h3>"
                  "<p>see game list!</p>"
                  "<h3>/chat?game_id=xx&word=xx</h3>"
                  "<p>you say word!</p>"
                  )))
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
