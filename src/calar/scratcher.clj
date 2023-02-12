(ns calar.scratcher
  (:require [calar.config :as config])
  (:require [clojure.string :as str])
  (:require [cheshire.core :as cheshire])
  (:require [clj-http.client :as client]))


(defn- request-schedule [edu-form department group]
  (client/get (str (:tracto-prefix config/config) "/schedule/"
                   edu-form "/"
                   department "/"
                   group)))


(defn scratch-lessons [edu-form department group]
  (-> (request-schedule edu-form department group)
      :body
      (cheshire/parse-string true)
      :lessons))


(def scratch-departments
  (-> (str (:tracto-prefix config/config) "/departments")
      client/get
      :body
      (cheshire/parse-string true)
      :departmentsList))


(defn find-subgroups [edn-schedule]
  (->> edn-schedule
       :lessons
       (map (comp str/trim :subGroup))
       (remove str/blank?)
       distinct
       sort))
