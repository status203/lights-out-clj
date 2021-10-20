(ns lights-out.domain-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [pjstadig.humane-test-output]

   [lights-out.domain :as sut]
   [lights-out.state.core :as los]
   [lights-out.validation :refer [check]]))

(deftest completed
  (is (= false (sut/completed? [false false true])))
  (is (= true (sut/completed? [false false false]))))

(deftest new-board
    (testing "board is correct size"
    (is (= 1 (->> (sut/new-board 1) ::los/grid-size))))
  (testing "board validates (number and type of cells)"
    (is (nil? (check ::los/board (sut/new-board 3)))))
  (testing "no move yet"
    (is (->> (sut/new-board 3) ::los/move nil?))))

(deftest toggling
  (let [board {::los/grid-size 3
               ::los/grid (into [] (repeat 9 true))}]
    (testing "correct cells toggled"
      (is (= [false false true
              false  true true
              true  true  true]
             (->> (sut/toggle-cell-in-board board 0) ::los/grid)))
      (is (= [true  false true
              false false false
              true  false true]
             (->> (sut/toggle-cell-in-board board 4) ::los/grid)))
      (is (= [true  true  true
              true  true  false
              true  false false]
             (->> (sut/toggle-cell-in-board board 8) ::los/grid))))
    (testing "move updated when completed"
      (let [near-board {::los/grid-size 2
                        ::los/grid [true true
                                    true false]}]
        (is (= nil (-> near-board (sut/toggle-cell-in-board 0) ::los/move)))))))

(deftest cell->label
  (is (= "A1" (sut/cell->label 0 5)))
  (is (= "Z26" (sut/cell->label 675 26))))