(in-ns 'vschat4relay.core.handler)

(defn get-game [id]
  (filter (fn [x] (= id (:id x))) @games))

(defn xor-get-game [id]
  (filter (fn [x] (not= id (:id x))) @games))

(defn run? [id]
  (apply :run? (get-game id)))

(defn remove-game [id]
    (reset! games (filter (fn [x] (not= id (:id x))) @games)))

(defn stop[id]
  (do
    (reset! games (let [target (get-game id)](conj (xor-get-game id) {:id id :run? false})))
    (remove-game id)
    (res-json (str "{\"id\": \"" id "\", \"run?\": \"false\"}"))
    ))

(defn start[id]
  (do
    (swap! games conj {:id id :run? true})
    (future (while (run? id) (do(regist-chat id))))
    (res-json (str "{\"id\": \"" id "\", \"run?\": \"true\"}"))
    ))
