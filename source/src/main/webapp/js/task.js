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

/**
 * タスク一覧画面のjQuery検索。
 * DataTablesで検索、クリア、自分のタスク、サマリー集計を行う。
 */
jQuery(function ($) {

    // DataTablesの日本語表示を設定する
    $.extend($.fn.dataTable.defaults, {
        language: {
            url: 'https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json',
            emptyTable: '表示できるタスクがありません',
            zeroRecords: '検索条件に一致するタスクがありません'
        }
    });

    // タスク一覧テーブルをDataTables化する
    const table = $('#foo-table').DataTable({
        paging: true,
        pageLength: 6,
        lengthChange: false,
        searching: true,
        ordering: false,
        info: false,
        autoWidth: false,
        dom: 'rtp',
        columnDefs: [
            {
                targets: 8,
                orderable: false,
                searchable: false
            }
        ]
    });

    // 案件と担当者のselectが空の場合はテーブルから候補を作る
    setupSelectOptionsFromTable(table, '#task-project-filter', 0);
    setupSelectOptionsFromTable(table, '#task-manager-filter', 2);

    // 案件、担当者、ステータス、優先度、自分のタスクの絞り込み条件を追加する
    $.fn.dataTable.ext.search.push(function (settings, data) {

        if (settings.nTable.id !== 'foo-table') {
            return true;
        }

        const selectedProject = $('#task-project-filter').val();
        const selectedManager = $('#task-manager-filter').val();
        const selectedStatus = $('#task-status-filter').val();
        const selectedPriority = $('#task-priority-filter').val();

        const myTaskActive = $('#my-task-button').attr('data-active') === 'true';
        const loginUserName = $('#login-user-name').val();

        const rowProject = normalizeText(data[0]);
        const rowManager = normalizeText(data[2]);
        const rowStatus = normalizeText(data[3]);
        const rowPriority = normalizeText(data[4]);

        if (selectedProject && rowProject !== selectedProject) {
            return false;
        }

        if (selectedManager && rowManager !== selectedManager) {
            return false;
        }

        if (selectedStatus && rowStatus !== selectedStatus) {
            return false;
        }

        if (selectedPriority && rowPriority !== selectedPriority) {
            return false;
        }

        if (myTaskActive && rowManager !== loginUserName) {
            return false;
        }

        return true;
    });

    // 検索ボタン押下で検索する
    $('#task-search-button').on('click', function () {
        applyTaskSearch(table);
    });

    // キーワード入力時にも検索する
    $('#task-keyword-filter').on('keyup', function () {
        applyTaskSearch(table);
    });

    // Enterで画面送信されないようにする
    $('#task-keyword-filter').on('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            applyTaskSearch(table);
        }
    });

    // select変更時に検索する
    $('#task-project-filter, #task-manager-filter, #task-status-filter, #task-priority-filter').on('change', function () {
        applyTaskSearch(table);
    });

    // クリアボタンで条件を初期化する
    $('#task-clear-button').on('click', function () {

        $('#task-keyword-filter').val('');
        $('#task-project-filter').val('');
        $('#task-manager-filter').val('');
        $('#task-status-filter').val('');
        $('#task-priority-filter').val('');

        $('#my-task-button')
            .attr('data-active', 'false')
            .removeClass('active');

        table.search('');
        table.draw();

        updateTaskSummary(table);
    });

    // 自分のタスクボタンで絞り込みを切り替える
    $('#my-task-button').on('click', function () {

        const button = $(this);
        const isActive = button.attr('data-active') === 'true';

        button.attr('data-active', String(!isActive));
        button.toggleClass('active', !isActive);

        applyTaskSearch(table);
    });

    // DataTables描画後にサマリーを更新する
    table.on('draw', function () {
        updateTaskSummary(table);
    });

    // 初期表示時にサマリーを更新する
    updateTaskSummary(table);
});

/**
 * DataTablesへ検索条件を反映する。
 *
 * @param {DataTables.Api} table DataTables
 */
function applyTaskSearch(table) {

    const keyword = $('#task-keyword-filter').val();

    table.search(keyword);
    table.draw();

    updateTaskSummary(table);
}

/**
 * 表示中タスクを集計してサマリーカードへ反映する。
 *
 * @param {DataTables.Api} table DataTables
 */
function updateTaskSummary(table) {

    const rows = table.rows({
        search: 'applied'
    }).data();

    let totalCount = 0;
    let progressCount = 0;
    let overdueCount = 0;
    let doneCount = 0;

    rows.each(function (row) {

        const status = normalizeText(row[3]);
        const dueDate = normalizeText(row[5]);

        totalCount++;

        if (status === '進行中') {
            progressCount++;
        }

        if (status === '完了') {
            doneCount++;
        }

        if (isOverdueTask(dueDate, status)) {
            overdueCount++;
        }
    });

    $('#taskTotalCount').text(totalCount);
    $('#taskProgressCount').text(progressCount);
    $('#taskOverdueCount').text(overdueCount);
    $('#taskDoneCount').text(doneCount);
}

/**
 * selectの候補がない場合にテーブル列から候補を追加する。
 *
 * @param {DataTables.Api} table DataTables
 * @param {string} selectId selectのID
 * @param {number} columnIndex 対象列番号
 */
function setupSelectOptionsFromTable(table, selectId, columnIndex) {

    const select = $(selectId);

    if (select.length === 0) {
        return;
    }

    if (select.find('option').length > 1) {
        return;
    }

    const values = [];

    table.rows().data().each(function (row) {

        const value = normalizeText(row[columnIndex]);

        if (value && values.indexOf(value) === -1) {
            values.push(value);
        }
    });

    values.sort();

    values.forEach(function (value) {
        select.append($('<option>').val(value).text(value));
    });
}

/**
 * 期限超過か判定する。
 *
 * @param {string} dueDate 期限日
 * @param {string} status ステータス
 * @returns {boolean} 期限超過ならtrue
 */
function isOverdueTask(dueDate, status) {

    if (!dueDate || status === '完了') {
        return false;
    }

    const normalizedDueDate = dueDate.replace(/\//g, '-');
    const targetDate = new Date(normalizedDueDate);

    if (Number.isNaN(targetDate.getTime())) {
        return false;
    }

    const today = new Date();

    today.setHours(0, 0, 0, 0);
    targetDate.setHours(0, 0, 0, 0);

    return targetDate < today;
}

/**
 * HTML混じりの文字列からテキストだけ取り出す。
 *
 * @param {string} value 変換前文字列
 * @returns {string} 変換後文字列
 */
function normalizeText(value) {

    if (value === null || value === undefined) {
        return '';
    }

    return $('<div>').html(value).text().trim();
}