# Duct と re-frame による「REST API + SPA開発」入門

・サーバサイドWebフレームワーク「Duct」<br>
・Reactラッパー「Reagent」を基礎としたフレームワーク「re-frame」<br>

これらの技術要素を用いて「REST API」と「SPA」という構成で「TODOアプリ」を作る、という有用なサンプルがあったのですが、それを自分なりに咀嚼して、ちゃんとお腹に落とすべく悪戦苦闘した記録です。<br>

ルーティングには、Ataraxy ではなく Compojure をもちいてます。<br>

### 概要

・step1  ... API 側：ブラウザから入力された url に対応する処理の結果を JSON 形式に変換したうえで、response に格納する。 APP 側：API 側の url にアクセスして、取得した response から JSON 形式のデータを取得し、整形、表示する。<br>

・step2  ... API 側：DB にたいして CRUD を実行する。 APP 側：パラメータ（PK、更新値）を request に設定し、CRUD に対応する API 側の url にアクセスする。API 側から処理結果を取得し、画面の再描画をする。<br>

・step3  ... API 側：「step2」と同じなので、割愛。 APP 側：「step2」に UI フレームワークを導入したもの。<br>

### 課題

・入力値の妥当性チェックを、どこで行うべきか？<br>
・API 側で行うことと仮定したうえで、エラーメッセージなどを UI フレームワークとどのようにしてひもづけられるか？<br>

## Developing

```sh

;; terminal1 : api side

$ pwd
~/todo-api

$ lein repl

user=> (dev)
:loaded

dev=> (go)
:duct.server.http.jetty/starting-server {:port 3000}
:initiated

```

```sh

;; terminal2 : app side

$ pwd
~/todo-app

$ npm install

$ npx shadow-cljs watch app

```

## References

- 「[ClojureのDuctとClojureScriptのre-frameによるREST API + SPA開発入門][1]」<br>
 [ `https://asukiaaa.blogspot.com/2017/12/clojureductcrud.html` ]<br>

[1]: https://qiita.com/lagenorhynque/items/38537fa91300e0ac0070
