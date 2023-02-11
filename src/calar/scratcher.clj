(ns calar.scratcher
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as str])
  (:require [cheshire.core :as cheshire])
  (:require [clj-http.client :as client]))


(def config
  (-> "resources/config.edn" slurp edn/read-string))


(defn get-schedule [edu-form, department, group]
  (client/get (str (:tracto-prefix  config) "/schedule/"
                   edu-form "/"
                   department "/"
                   group)))


(defn get-edn-schedule [edu-form, department, group]
  (cheshire/parse-string (:body (get-schedule edu-form department group)) true))


(def get-departments
  (-> (str (:tracto-prefix config) "/departments")
      client/get
      :body
      (cheshire/parse-string true)
      :departmentsList))


(defn get-subgroups [edn-schedule]
  (->> edn-schedule
      :lessons
      (map (comp str/trim :subGroup))
      (remove str/blank?)))
