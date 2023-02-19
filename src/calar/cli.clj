(ns calar.cli
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [calar.config :as config]
            [calar.scratcher :as scratcher]))


(def ^:private cli-options
  [["-d" "--department DEPARTMENT" "Department inside SSU"]
   ["-g" "--group GROUP" "Study group inside department"]
   ["-h" "--help"]])


(defn- missing-required?
  "Returns true if opts is missing any of the required-opts"
  [opts]
  (let [required-opts #{:department :group}]
    (not-every? (-> opts keys set) required-opts)))


(defn- usage [options-summary]
  (->> [(:description config/config)
        ""
        "Options:"
        options-summary]
       (str/join \newline)))


(defn- error-msg [errors]
  (str "ERROR: "
       (str/join ", " errors)))


(defn exit [status msg]
  (println msg)
  (System/exit status))


(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with an error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options _ summary errors]} (cli/parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (usage summary) :ok? true}
      errors
      {:exit-message (error-msg errors) :ok? false}
      (missing-required? options)
      {:exit-message (error-msg ["Not all information was provided"]) :ok? false}
      (not-any? #{(:department options)} (map :url scratcher/scratch-departments))
      {:exit-message (error-msg ["Unknown department"]) :ok? false}
      :else
      {:options options})))
