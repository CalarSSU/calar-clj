(ns calar.core
  (:require [calar.scratcher :as scratcher]
            [calar.converter :as converter]
            [calar.cli :as cli])
  (:gen-class))


(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (cli/validate-args args)]
    (if exit-message
      (cli/exit (if ok? 0 1) exit-message)
      (-> (scratcher/scratch-lessons options)
          converter/edn->ical
          println))))
