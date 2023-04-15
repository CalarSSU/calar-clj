(ns calar.config
  (:require [clojure.edn :as edn]
            [calar.config :as config]))


(def config
  "Config parsed into EDN"
  (-> "resources/config.edn" slurp edn/read-string))


(defn- get-semester [semestre]
  (-> config
      :semesters
      semestre))


(def cur-semester
  "Current semester: start and end dates"
  (-> config
      :semesters
      :current
      get-semester))
