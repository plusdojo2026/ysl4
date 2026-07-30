'use strict';

/**
 * 案件一覧画面の処理。
 * jQueryとDataTablesで検索、クリア、件数表示を制御する。
 */
jQuery(function ($) {

	// DataTablesの日本語表示を設定する
	$.extend($.fn.dataTable.defaults, {
		language: {
			url: 'https://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json',
			emptyTable: '表示できる案件がありません',
			zeroRecords: '検索条件に一致する案件がありません'
		}
	});

	// 案件一覧テーブルをDataTables化する
	const table = $('#project-table').DataTable({
		paging: true,
		pageLength: 6,
		lengthChange: false,
		searching: true,
		ordering: true,
		info: false,
		autoWidth: false,
		dom: 'rtp',
		columnDefs: [
			{
				targets: 10,
				orderable: false,
				searchable: false
			}
		]
	});

	// ステータスと優先度の独自検索を追加する
	$.fn.dataTable.ext.search.push(function (settings, data) {

		// 案件一覧以外には適用しない
		if (settings.nTable.id !== 'project-table') {
			return true;
		}

		// 検索条件を取得する
		const selectedStatus = $('#project-status-filter').val();
		const selectedPriority = $('#project-priority-filter').val();

		// テーブルの列データを取得する
		const rowStatus = normalizeProjectText(data[3]);
		const rowPriority = normalizeProjectText(data[4]);

		// ステータスが指定されていて一致しない場合は非表示にする
		if (selectedStatus && rowStatus !== selectedStatus) {
			return false;
		}

		// 優先度が指定されていて一致しない場合は非表示にする
		if (selectedPriority && rowPriority !== selectedPriority) {
			return false;
		}

		return true;
	});

	// キーワード入力時に検索する
	$('#project-keyword-filter').on('keyup', function () {
		applyProjectSearch(table);
	});

	// Enterキーで画面送信されないようにする
	$('#project-keyword-filter').on('keydown', function (event) {

		if (event.key === 'Enter') {
			event.preventDefault();
			applyProjectSearch(table);
		}
	});

	// ステータス変更時に検索する
	$('#project-status-filter').on('change', function () {
		applyProjectSearch(table);
	});

	// 優先度変更時に検索する
	$('#project-priority-filter').on('change', function () {
		applyProjectSearch(table);
	});

	// クリアボタンで検索条件を初期化する
	$('#project-clear-button').on('click', function () {

		// 入力値を初期化する
		$('#project-keyword-filter').val('');
		$('#project-status-filter').val('');
		$('#project-priority-filter').val('');

		// DataTables側の検索条件も初期化する
		table.search('');
		table.draw();

		// 件数を更新する
		updateProjectCounts(table);
	});

	// 描画後に件数を更新する
	table.on('draw', function () {
		updateProjectCounts(table);
	});

	// 初期表示時に件数を更新する
	updateProjectCounts(table);
});

/**
 * 案件検索条件をDataTablesへ反映する。
 *
 * @param {DataTables.Api} table DataTables
 */
function applyProjectSearch(table) {

	// キーワードを取得する
	const keyword = $('#project-keyword-filter').val();

	// DataTablesの全体検索へ反映する
	table.search(keyword);
	table.draw();

	// 件数を更新する
	updateProjectCounts(table);
}

/**
 * 表示中の案件を基に件数を更新する。
 *
 * @param {DataTables.Api} table DataTables
 */
function updateProjectCounts(table) {

	// 検索適用後の行を取得する
	const appliedRows = table.rows({
		search: 'applied'
	}).data();

	// 全行を取得する
	const allRows = table.rows().data();

	let displayCount = 0;
	let totalCount = 0;
	let progressCount = 0;
	let doneCount = 0;
	let stopCount = 0;

	// 全件数を数える
	allRows.each(function () {
		totalCount++;
	});

	// 表示中の件数とステータス別件数を数える
	appliedRows.each(function (row) {

		const status = normalizeProjectText(row[3]);

		displayCount++;

		if (status === '進行中') {
			progressCount++;
		}

		if (status === '完了') {
			doneCount++;
		}

		if (status === '中止') {
			stopCount++;
		}
	});

	// 画面へ反映する
	$('#project-display-count').text(displayCount);
	$('#project-total-count').text(totalCount);
	$('#project-progress-count').text(progressCount);
	$('#project-done-count').text(doneCount);
	$('#project-stop-count').text(stopCount);
}

/**
 * HTML文字列から検索用テキストを取り出す。
 *
 * @param {string} value 変換前の値
 * @return {string} 変換後の値
 */
function normalizeProjectText(value) {

	if (value === null || value === undefined) {
		return '';
	}

	return $('<div>').html(value).text().trim();
}