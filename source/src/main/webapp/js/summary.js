'use strict';

/**
 * 月次集計画面の処理.
 * DataTablesで各一覧を表示し、工数明細はjQueryで検索する.
 */
jQuery(function ($) {

    // DataTablesの日本語表示を設定する
    $.extend($.fn.dataTable.defaults, {
        language: {
            url: 'https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json',
            emptyTable: '表示できるデータがありません',
            zeroRecords: '検索条件に一致するデータがありません'
        }
    });

    // 案件別工数テーブルをDataTables化する
    $('#project-summary-table').DataTable({
        paging: true,
        pageLength: 5,
        lengthChange: false,
        searching: true,
        ordering: false,
        info: false,
        autoWidth: false,
        dom: 'rtp'
    });

    // メンバー別工数テーブルをDataTables化する
    $('#member-summary-table').DataTable({
        paging: true,
        pageLength: 5,
        lengthChange: false,
        searching: true,
        ordering: false,
        info: false,
        autoWidth: false,
        dom: 'rtp'
    });

    // 工数明細テーブルをDataTables化する
    const workLogTable = $('#worklog-summary-table').DataTable({
        paging: true,
        pageLength: 10,
        lengthChange: false,
        searching: true,
        ordering: false,
        info: false,
        autoWidth: false,
        dom: 'rtp'
    });

    // 表示ボタンとCSV出力ボタンの入力確認を設定する
    $('#monthlySummaryForm').on('submit', function (event) {

        const targetMonth = $('#targetMonth').val();

        if (!targetMonth) {
            alert('対象月を選択してください');
            event.preventDefault();
            return;
        }
    });

    // 工数明細のキーワード検索を行う
    $('#worklog-keyword-filter').on('keyup', function () {
        workLogTable.search(this.value).draw();
    });

    // Enterで画面送信されないようにする
    $('#worklog-keyword-filter').on('keydown', function (event) {

        if (event.key === 'Enter') {
            event.preventDefault();
            workLogTable.search(this.value).draw();
        }
    });

    // 明細検索条件を初期化する
    $('#worklog-clear-button').on('click', function () {

        $('#worklog-keyword-filter').val('');

        workLogTable.search('');
        workLogTable.draw();
    });
});