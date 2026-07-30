'use strict';

/**
 * タスク詳細画面の処理.
 * 工数入力モーダルと入力チェックを担当する.
 */
document.addEventListener('DOMContentLoaded', function () {

    // 工数入力モーダルを開くボタンを取得する
    const openButton = document.getElementById('openWorkLogModalButton');

    // 工数入力モーダルを閉じるボタンを取得する
    const closeButton = document.getElementById('closeWorkLogModalButton');

    // 工数入力モーダルを取得する
    const modal = document.getElementById('workLogModal');

    // 工数入力フォームを取得する
    const form = document.getElementById('workLogForm');

    // クリアボタンを取得する
    const clearButton = document.getElementById('clearWorkLogButton');

    // 作業日を取得する
    const workDate = document.getElementById('workDate');

    // 初期表示時に作業日へ今日を入れる
    if (workDate && !workDate.value) {
        workDate.value = getTodayText();
    }

    // 開くボタンがある場合だけ設定する
    if (openButton && modal) {
        openButton.addEventListener('click', function () {
            modal.classList.add('show');

            if (workDate) {
                workDate.focus();
            }
        });
    }

    // 閉じるボタンがある場合だけ設定する
    if (closeButton && modal) {
        closeButton.addEventListener('click', function () {
            modal.classList.remove('show');
        });
    }

    // 背景クリックで閉じる
    if (modal) {
        modal.addEventListener('click', function (event) {
            if (event.target === modal) {
                modal.classList.remove('show');
            }
        });
    }

    // クリアボタンでフォームを初期化する
    if (clearButton && form) {
        clearButton.addEventListener('click', function () {
            form.reset();

            if (workDate) {
                workDate.value = getTodayText();
            }
        });
    }

    // 登録前の入力チェックを行う
    if (form) {
        form.addEventListener('submit', function (event) {
            if (!validateWorkLogForm()) {
                event.preventDefault();
            }
        });
    }
});

/**
 * 工数入力フォームを確認する.
 * @return {boolean} 正常ならtrue.
 */
function validateWorkLogForm() {

    const workDate = document.getElementById('workDate');
    const manHours = document.getElementById('manHours');
    const jobContents = document.getElementById('jobContents');

    if (!workDate || !workDate.value) {
        alert('作業日を入力してください');
        return false;
    }

    if (!manHours || !manHours.value) {
        alert('工数を入力してください');
        return false;
    }

    const manHoursValue = Number(manHours.value);

    if (Number.isNaN(manHoursValue)) {
        alert('工数は数値で入力してください');
        return false;
    }

    if (manHoursValue < 0.5 || manHoursValue > 24) {
        alert('工数は0.5から24の範囲で入力してください');
        return false;
    }

    if (manHoursValue * 2 !== Math.floor(manHoursValue * 2)) {
        alert('工数は0.5単位で入力してください');
        return false;
    }

    if (jobContents && jobContents.value.length > 255) {
        alert('作業内容は255文字以内で入力してください');
        return false;
    }

    return true;
}

/**
 * 今日の日付をyyyy-MM-ddで取得する.
 * @return {string} 今日の日付.
 */
function getTodayText() {

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

    return year + '-' + month + '-' + day;
}