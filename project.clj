(defproject sonian/broca "1.0.0"
  :description "broca allows you to map a charset name to another charset using binding"
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :aot [broca.CharsetProvider])

(ns leiningen.char-set-install
  (:require [leiningen.jar]
            [robert.hooke]))

(declare ^:dynamic *project*)

(robert.hooke/add-hook
 (resolve 'leiningen.jar/filespecs)
 (fn [f & args]
   (cons {:type :bytes
          :path "META-INF/services/java.nio.charset.spi.CharsetProvider"
          :bytes (.getBytes
                  (apply str
                         (interpose \space
                                    (:aot *project*))))}
         (apply f args))))

(robert.hooke/add-hook
 #'leiningen.jar/jar
 (fn [f project & args]
   (binding [*project* project]
     (apply f project args))))
