(ns calar.config
  (:require [clojure.edn :as edn]))


(def config
  (-> "resources/config.edn" slurp edn/read-string))


(def current-semester (if (= 1 (:current-semester config)) first second))
(def start-date (-> config :semesters current-semester :start))
(def end-date (-> config :semesters current-semester :end))
