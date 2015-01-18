# vschat4relay

システムvsシステムの雑談ゲーム実施するアプリケーションの中継機能です。

## API一覧

### /

アプリルートです。下のように表示されていれば稼働しています。

<pre>running</pre>

### /botlist

利用できるbotの一覧を取得します。

応答：
<pre>
[{
    "bot_id": "xx"
    , "bot_name": "xx"
    , "profile": "xx"
    , "picture_url": "xx"
}]
</pre>

### /start?bot_id1=xx&bot_id2=xx&start=xx&goal=xx

ゲームを開始します。
ゲームの経過はDBに格納します。
bot_id1：プレイヤー１として設定するbot_idを指定します。
bot_id2：プレイヤー２として設定するbot_idを指定します。
start：開始ワードを指定します。
goal：ゲーム終了条件のワードを指定します。


応答：
<pre>
{
    "game_id": "xx"
    "run?": true
}
</pre>

経過出力先テーブルレイアウト
<pre>
create table chat_log (
 id serial primary key
 , game_id varchar(128)
 , bot_id varchar(128)
 , word varchar(512)
 , ins_time timestamp default CURRENT_TIMESTAMP
)
</pre>

### /stop?game_id=xx

ゲームを終了します。
game_id：終了させるgame_idを指定します。

応答：
<pre>
{
    "game_id": ""
    "run?": false
}
</pre>


### /gamelist

実行中のゲーム一覧を取得します。

応答：
<pre>
[
    {
        "id":1
        ,"run?":true
        ,"turn":1
        ,"bot-1":1
        ,"bot-2":1
        ,"curr-word":"かもめがかわいいですね"
        ,"goal-word":"おいしい"
    }
]
</pre>

### /chat?game_id=xx&word=xx

ゲーム中の会話に割り込みます。
game_id：割り込み対象のgame_idを指定します。
word：割り込むワードを指定します。

応答：
<pre>
{
    "id":1
    ,"run?":true
    ,"turn":1
    ,"bot-1":1
    ,"bot-2":1
    ,"curr-word":"かもめがかわいいですね"
    ,"goal-word":"おいしい"
}
</pre>

### /log?game_id

会話ログを取得します。
game_id：取得対象のgame_idを指定します。

応答：
<pre>
[
    {
        "ins_time":"2015-01-17T12:28:58Z"
        ,"picture_url":"xxx"
        ,"word":"今年はリフレッシュ休暇とれるかな？"
        ,"bot_id":"1"
        ,"game_id":"AoXTT0QDTFKznTYnwEjSBGyxXN8zmC"
        ,"id":188
    }
]
</pre>

### /logall

全ての会話ログを取得します

応答：
<pre>
[
    {
        "ins_time":"2015-01-17T12:28:58Z"
        ,"picture_url":"xxx"
        ,"word":"今年はリフレッシュ休暇とれるかな？"
        ,"bot_id":"1"
        ,"game_id":"AoXTT0QDTFKznTYnwEjSBGyxXN8zmC"
        ,"id":188
    }
]
</pre>


### /delete?game_id

会話ログを削除します。
game_id：削除対象のgame_idを指定します。

応答：
<pre>
ame_id=xxのログをテーブルから削除しました
</pre>

### /deleteall

全ての会話ログを削除します。

応答：
<pre>
ログをテーブルをクリアしました
</pre>

## License

Copyright © 2015 hisataka
