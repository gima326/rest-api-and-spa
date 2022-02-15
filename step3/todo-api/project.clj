(defproject todo-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [duct/core "0.8.0"]
                 [duct/module.logging "0.5.0"]

                 ;; add
                 [duct/module.sql "0.6.1"]
                 [duct/module.web "0.7.3"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [mysql/mysql-connector-java "8.0.22"]

                 [compojure "1.6.2"]
                 [ring-cors "0.1.13"]

                 ]
  :plugins [[duct/lein-duct "0.12.3"]]
  :main ^:skip-aot todo-api.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware]
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[integrant/repl "0.3.2"]
                                   [hawk "0.2.11"]
                                   [eftest "0.5.9"]

                                   ;; add
                                   [phrase "0.3-alpha4"]
                                   [ring/ring-mock "0.3.2"]
                                   [com.gearswithingears/shrubbery "0.4.1"]

                                   ;; add3
                                   [orchestra "2020.07.12-1"]

                                   ;; add4
                                   [ring/ring-core "1.6.1"]
                                   [org.clojure/data.json "2.4.0"]

                                   ]

                  ;; add2
                  ;;:test-selectors {:s :static :d :dynamic :s2 :static2 :d2 :dynamic2}
                  }})
