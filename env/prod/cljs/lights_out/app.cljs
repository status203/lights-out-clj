(ns lights-out.app
  (:require [lights-out.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
