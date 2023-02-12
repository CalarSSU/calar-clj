(ns calar.core
  (:require [calar.config :as config])
  (:require [calar.scratcher :as scratcher])
  (:require [calar.converter :as converter])
  (:gen-class))


(defn -main []
  (-> (scratcher/scratch-lessons (-> config/config :forms :do) "knt" "351")
      converter/edn->ical
      println))
