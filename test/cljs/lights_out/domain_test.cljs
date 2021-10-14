(ns lights-out.domain-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [pjstadig.humane-test-output]

   [lights-out.domain :as sut]
   [lights-out.state.core :as los]))

(deftest completed
  (is (= false (sut/completed? [false false true])))
  (is (= true (sut/completed? [false false false]))))

(deftest new-game
    (testing "grid-size copied in to game correctly"
    (is (= 1 (->> (sut/new-game 1) ::los/board ::los/grid-size))))
  (testing "grid is expected size"
    (is (= 9 (->> (sut/new-game 3) ::los/board ::los/grid count))))
  (testing "cells are all boolean"
    (is (->> (sut/new-game 4) ::los/board ::los/grid (every? boolean?)))))

(deftest toggling
  (let [game {::los/history []
               ::los/board {::los/grid-size 3
                            ::los/grid (into [] (repeat 9 true))}}]
    (testing "correct cells toggled"
      (is (= [false false true
              false  true true
              true  true  true]
             (->> (sut/toggle-cell-in-game game 0) ::los/board ::los/grid)))
      (is (= [true  false true
              false false false
              true  false true]
             (->> (sut/toggle-cell-in-game game 4) ::los/board ::los/grid)))
      (is (= [true  true  true
              true  true  false
              true  false false]
             (->> (sut/toggle-cell-in-game game 8) ::los/board ::los/grid))))
    (testing "move appended to history"
      (let [expected {::los/move 4
                      ::los/post-move-grid [true  false true
                                            false false false
                                            true  false true]}]
        (is (= expected (first (::los/history (sut/toggle-cell-in-game game 4)))))))))

(deftest cell->label
  (is (= "A1" (sut/cell->label 0 5)))
  (is (= "Z26" (sut/cell->label 675 26))))