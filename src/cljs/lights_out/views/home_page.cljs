(ns lights-out.views.home-page
  (:require [re-frame.core :as rf]

            [lights-out.domain :refer [cell->label]]))

(defn choose-size []
  (let [grid-size @(rf/subscribe [:setup/grid-size])]
    [:div.field.is-horizontal
     [:div.field-label.is-normal>label.label "Grid Size"]
     [:div.field-body>div.field.is-narrow>p.control
      [:input.input {:type "number"
                     :min 1
                     :max 26
                     :on-change #(rf/dispatch [:setup/size-changed (-> % .-target .-value int)])
                     :value grid-size}]]]))

(defn setup-errors [errors]
  (into [:div.setup-errors]
        (map (fn [error] [:div.setup-error error]) errors)))

(defn setup []
  (let [errors @(rf/subscribe [:setup/errors])]
    [:div.box
     [choose-size]
     [:div.control>a.button.is-primary {:on-click #(rf/dispatch [:setup/new-game])} "New Game"]
     (when errors [setup-errors errors])]))

(defn grid-cell
  [completed? index lit]
  (let [click-handler (when-not completed? #(rf/dispatch [:game/toggle-cell index]))
        enter-handler (when-not completed? #(rf/dispatch [:game/enter-cell index]))
        leave-handler  (when-not completed?  #(rf/dispatch [:game/leave-cell]))]
    ^{:key (str index)}
    [:div.cell {:class (if lit "lit" "unlit")

                :on-click click-handler
                :on-mouse-enter enter-handler
                :on-mouse-leave leave-handler}]))

(defn show-board []
  (let [{:keys [:grid-size :grid :move-label :completed?]} @(rf/subscribe [:display/board])]
    [:div.column>div.box>div.columns
     [:div.square
      (when completed? [:div.success "Success. Now where's the light switch?"])
      [:div.grid-container
       (into [:div.grid
              {:style {:grid-template-columns (str "repeat(" grid-size ", 1fr)")
                       :grid-template-rows    (str "repeat(" grid-size ", 1fr)")}}]
             (map-indexed (partial grid-cell completed?) grid))]]
     [:div.column.is-narrow>div#hovered-cell move-label]]))

(defn history-move
  [idx]
  (let [{:keys [:move-label :entry]} @(rf/subscribe [:history/entry idx])
        enter-handler #(rf/dispatch [:display/historical entry])
        leave-handler #(rf/dispatch [:display/stored])]
    ^{:key (str idx)} [:li {:class [(when (zero? idx) "start-position")]

                            :on-mouse-enter enter-handler
                            :on-mouse-leave leave-handler}
                       move-label]))

(defn show-history []
  (let [entries-count @(rf/subscribe [:history/count])]
    [:div.column.is-narrow>div.box
     [:h2 "Moves"]
     (into [:ol.history]
           (map history-move (range entries-count)))]))

(defn when-game []
  (let [display @(rf/subscribe [:display/board])]
    (when display
     [:div.columns
      [show-history]
      [show-board]])))


(defn home-page []
  [:section.section>div.container>div.content
   [setup]
   [when-game]])

