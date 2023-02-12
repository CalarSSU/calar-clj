(ns calar.converter
  (:require [calar.config :as config])
  (:require [clj-icalendar.core :as ical]))


(def empty-ical
  (let [ical-config (config/config :ical)]
    (ical/create-cal (:author ical-config)
                     (:title ical-config)
                     (:version ical-config)
                     (:lang ical-config))))


(import java.util.Date)
(defn parse-time [lesson-time week-day]
  [(Date. (.getYear (Date.))
          (:month config/start-date)
          (+ (dec week-day) (:day config/start-date))
          (:hourStart lesson-time)
          (:minuteStart lesson-time))
   (Date. (.getYear (Date.))
          (:month config/start-date)
          (+ (dec week-day) (:day config/start-date))
          (:hourEnd lesson-time)
          (:minuteEnd lesson-time))])


(defn create-lesson [lesson]
  (let [title (:name lesson)
        [start end] (parse-time (:lessonTime lesson)
                                (-> lesson :day :dayNumber))]
    (ical/create-event start
                       end
                       title
                       :organizer (-> config/config :ical :organizer))))


(defn edn->ical [lessons]
 (let [cal empty-ical
       ical-events (map create-lesson lessons)
       _ (reduce (fn [cal event] (ical/add-event! cal event)) cal ical-events)]
   (ical/output-calendar cal)))
