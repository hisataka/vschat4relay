(in-ns 'vschat4relay.core.handler)

(defn get-game [id]
  (filter (fn [x] (= id (:id x))) @games))

(defn xor-get-game [id]
  (filter (fn [x] (not= id (:id x))) @games))

(defn get-run? [id]
  (apply :run? (get-game id)))

(defn get-turn [id]
  (apply :turn (get-game id)))

(defn get-curr-word [id]
  (apply :curr-word (get-game id)))

(defn get-goal-word [id]
  (apply :goal-word (get-game id)))

(defn remove-game [id]
    (reset! games (filter (fn [x] (not= id (:id x))) @games)))

(defn stop [id]
  (do
    (reset! games
            (let [target (get-game id)]
              (conj (xor-get-game id) {:id (apply :id target) :run? false :turn (apply :turn target) :bot-1 (apply :bot-1 target) :bot-2 (apply :bot-2 target) :curr-word (apply :curr-word target) :goal-word (apply :goal-word target)})))
    (remove-game id)
    (res-json (str "{\"game_id\": \"" id "\", \"run?\": false}"))
    ))

(defn change-turn [id]
  (reset! games
    (let [target (get-game id)]
      (conj
       (xor-get-game id)
       {:id (apply :id target) :run? (apply :run? target) :turn (if (= (apply :turn target) (apply :bot-1 target)) (apply :bot-2 target) (apply :bot-1 target)) :bot-1 (apply :bot-1 target) :bot-2 (apply :bot-2 target) :curr-word (apply :curr-word target) :goal-word (apply :goal-word target)}
       ))))

(defn upd-curr-word [id new-word]
  (reset! games
    (let [target (get-game id)]
      (conj
       (xor-get-game id)
       {:id (apply :id target) :run? (apply :run? target) :turn (apply :turn target) :bot-1 (apply :bot-1 target) :bot-2 (apply :bot-2 target) :curr-word new-word :goal-word (apply :goal-word target)}
       ))))

(defn get-answer [bot-id keyword]
  (:answer
   (parse-string
    (let [res
          (http/get
           (str "https://fummy-jokebot-service.herokuapp.com/bot/reaction?bot_id=" bot-id "&keyword="
                (java.net.URLEncoder/encode keyword "UTF-8")))]
      (:body @res)) true)))

(defn start [bot-1 bot-2 curr-word goal-word id]
  (do
    (swap! games conj {:id id :run? true :turn bot-1 :bot-1 bot-1 :bot-2 bot-2 :curr-word curr-word :goal-word goal-word})
    (future (while (get-run? id) (do
                                   (Thread/sleep 5000)
                                   (regist-chat id (get-turn id) (get-curr-word id))
                                   (let [new-word (get-answer (get-turn id) (get-curr-word id))]
                                         (upd-curr-word id new-word)
                                         )
                                   (if (= nil (re-seq (re-pattern (str ".*" (get-goal-word id) ".*")) (get-curr-word id))) (change-turn id) (stop id))
                                   )))
    (res-json (str "{\"game_id\": \"" id "\", \"run?\": true}"))
    ))

(defn bot-list []
  (res-json (let [res (http/get "https://fummy-jokebot-service.herokuapp.com/bot/list")]
    (:body @res))))

(defn game-list []
  (res-json (generate-string @games)))

(defn chat [id word]
  (do
    (upd-curr-word id word)
    (res-json (generate-string (get-game id)))
    ))
