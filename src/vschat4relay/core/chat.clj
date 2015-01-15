(in-ns 'vschat4relay.core.handler)

; チャット用rest-api呼び出し用（本文のJSON文字列を返す）
(defn rest-chat [url req]
  (:body
   @(http/get
    (str url "?req=" (java.net.URLEncoder/encode req "UTF-8")))))
