# Duct と re-frame による「REST API + SPA開発」入門

・サーバサイドWebフレームワーク「Duct」<br>
・Reactラッパー「Reagent」を基礎としたフレームワーク「re-frame」<br>

これらの技術要素を用いて「REST API」と「SPA」という構成で「TODOアプリ」を作る、という有用なサンプルがあったのですが、それを自分なりに咀嚼して、ちゃんとお腹に落とすべく悪戦苦闘した記録です。<br>

ルーティングには、Ataraxy ではなく Compojure をもちいてます。<br>

### 概要

・step1  ... APP 側から API 側の url にアクセスして、response から取得した JSON 形式のデータを APP 側で整形、表示する。<br>



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

- 「[ClojureのDuctとClojureScriptのre-frameによるREST API + SPA開発入門][1]」 [ `https://asukiaaa.blogspot.com/2017/12/clojureductcrud.html` ]<br>

[1]: https://qiita.com/lagenorhynque/items/38537fa91300e0ac0070
