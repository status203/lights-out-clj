(ns lights-out.state.options
  (:require
   [re-frame.core :as rf]
   
   [lights-out.state.core :as los]))

;; Handlers



;; Subscriptions
(rf/reg-sub
 :options/options
 (fn [{{:keys [::los/hypothetical-highlight
               ::los/historical-highlight]} ::los/options} [_]]
   {::hypothetical-highlight hypothetical-highlight
    ::historical-highlight historical-highlight}))
