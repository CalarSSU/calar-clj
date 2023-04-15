(ns calar.converter
  (:require [calar.config :as config])
  (:require [clj-icalendar.core :as ical]))


(def empty-ical
  "Returns new empty iCal with metadata from config"
  (let [ical-config (config/config :ical)]
    (ical/create-cal (:author ical-config)
                     (:title ical-config)
                     (:version ical-config)
                     (:lang ical-config))))


(import java.util.Date)
(defn parse-time
  "Returns a pair of java.util.Date: start and end of lesson, using EDN from
   scratcher as input"
  [lesson-time week-day]
  (let [cur-year  (.getYear (Date.))
        cur-month (-> config/cur-semester :start :month)
        cur-day   (+ (dec week-day) (-> config/cur-semester :start :day))]
    [(Date. cur-year
            cur-month
            cur-day
            (:hourStart lesson-time)
            (:minuteStart lesson-time))
     (Date. cur-year
            cur-month
            cur-day
            (:hourEnd lesson-time)
            (:minuteEnd lesson-time))]))


(defn create-lesson
  "Returns new iCal event created from EDN lesson"
  [lesson]
  (let [title       (:name lesson)
        [start end] (parse-time (:lessonTime lesson)
                                (-> lesson :day :dayNumber))]
    (ical/create-event start
                       end
                       title
                       :organizer (-> config/config :ical :organizer))))


(defn edn->ical
  "Converts EDN lessons into iCal object ready for export"
  [edn-lessons]
  (let [cal          empty-ical
        ical-lessons (map create-lesson edn-lessons)
        _            (reduce (fn [cal event] (ical/add-event! cal event))
                             cal
                             ical-lessons)]
    (ical/output-calendar cal)))
