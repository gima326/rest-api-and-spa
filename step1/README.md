# Step1 -- アプリケーション間で固定データをやりとりする --

### 概要

・ブラウザに入力された URI と、HTTP リクエストメソッド（GET、POST など）により、対応する個別処理の実行、処理結果を返す、RESTful な挙動を目指している。<br>

・「API」側：サーバサイドWebフレームワーク「Duct」を用いている。<br>
・「APP」側：Reactラッパー「Reagent」を基礎としたフレームワーク「re-frame」を用いている。<br>

・ブラウザから入力された URI に該当する「APP」側の処理を呼び出す。<br>
・上記処理の内部から、さらに「API」側の処理を呼び出す（「API」用の URI と HTTP リクエストメソッドで処理を区別する。<br>
・「API」側の処理結果を取得し、そのデータを「APP」側で表示する。<br>
