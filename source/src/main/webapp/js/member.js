'use strict';

/**
 * メンバー一覧画面の処理。
 * DataTablesとjQueryで検索、集計、三点メニューを制御する。
 */
jQuery(function ($) {

    /**
     * DataTablesの日本語表示を設定する。
     */
    $.extend($.fn.dataTable.defaults, {
        language: {
            url: 'https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json'
        }
    });

    /**
     * メンバー一覧テーブルをDataTables化する。
     */
    const table = $('#member-table').DataTable({
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
                targets: 6,
                orderable: false,
                searchable: false
            }
        ]
    });

    /**
     * 権限と状態の独自検索を追加する。
     */
    $.fn.dataTable.ext.search.push(function (settings, data) {

        if (settings.nTable.id !== 'member-table') {
            return true;
        }

        const selectedRole = $('#role-filter').val();
        const selectedStatus = $('#status-filter').val();

        const rowRole = normalizeText(data[3]);
        const rowStatus = normalizeText(data[4]);

        if (selectedRole && rowRole !== selectedRole) {
            return false;
        }

        if (selectedStatus && rowStatus !== selectedStatus) {
            return false;
        }

        return true;
    });

    /**
     * 検索ボタン押下で検索する。
     */
    $('#member-search-button').on('click', function () {
        applySearch(table);
    });

    /**
     * キーワード入力中も検索する。
     */
    $('#keyword-filter').on('keyup', function () {
        applySearch(table);
    });

    /**
     * Enterキーで画面送信されないようにする。
     */
    $('#keyword-filter').on('keydown', function (event) {

        if (event.key === 'Enter') {
            event.preventDefault();
            applySearch(table);
        }
    });

    /**
     * 権限と状態の変更時に検索する。
     */
    $('#role-filter, #status-filter').on('change', function () {
        applySearch(table);
    });

    /**
     * クリアボタン押下で条件を初期化する。
     */
    $('#clear-search-button').on('click', function () {

        $('#keyword-filter').val('');
        $('#role-filter').val('');
        $('#status-filter').val('');

        table.search('');
        table.draw();

        updateVisibleCounts(table);
    });

    /**
     * 三点メニューを開閉する。
     */
    $(document).on('click', '.more-btn', function (event) {

        event.stopPropagation();

        const targetMenu = $(this).siblings('.more-menu');

        $('.more-menu').not(targetMenu).removeClass('show');

        targetMenu.toggleClass('show');
    });

    /**
     * 画面のどこかを押したら三点メニューを閉じる。
     */
    $(document).on('click', function () {
        $('.more-menu').removeClass('show');
    });

    /**
     * 三点メニュー内クリックでは閉じない。
     */
    $(document).on('click', '.more-menu', function (event) {
        event.stopPropagation();
    });

    /**
     * DataTables描画後に件数を再集計する。
     */
    table.on('draw', function () {
        updateVisibleCounts(table);
    });

    /**
     * 初期表示の件数を設定する。
     */
    updateVisibleCounts(table);
});

/**
 * 検索条件をDataTablesへ反映する。
 *
 * @param {DataTables.Api} table DataTablesオブジェクト。
 */
function applySearch(table) {

    const keyword = $('#keyword-filter').val();

    table.search(keyword);
    table.draw();

    updateVisibleCounts(table);
}

/**
 * 表示中データを基に件数カードを更新する。
 *
 * @param {DataTables.Api} table DataTablesオブジェクト。
 */
function updateVisibleCounts(table) {

    const rows = table.rows({
        search: 'applied'
    }).data();

    let totalCount = 0;
    let validCount = 0;
    let invalidCount = 0;
    let adminCount = 0;

    rows.each(function (row) {

        const role = normalizeText(row[3]);
        const status = normalizeText(row[4]);

        totalCount++;

        if (role === '管理者') {
            adminCount++;
        }

        if (status === '有効') {
            validCount++;
        }

        if (status === '無効') {
            invalidCount++;
        }
    });

    $('#totalMemberCount').text(totalCount);
    $('#validMemberCount').text(validCount);
    $('#adminMemberCount').text(adminCount);
    $('#invalidMemberCount').text(invalidCount);
}

/**
 * HTML文字列から検索用テキストを作る。
 *
 * @param {string} value 変換前文字列。
 * @returns {string} 変換後文字列。
 */
function normalizeText(value) {

    if (value === null || value === undefined) {
        return '';
    }

    return $('<div>').html(value).text().trim();
}