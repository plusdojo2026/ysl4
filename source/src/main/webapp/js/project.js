'use strict';

/**
 * 案件一覧のjQuery検索.
 * DataTablesでキーワード、ステータス、優先度を絞り込む.
 */
jQuery(function($) {

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
		ordering: false,
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

	// ステータスと優先度の絞り込み条件を追加する
	$.fn.dataTable.ext.search.push(function(settings, data) {

		if (settings.nTable.id !== 'project-table') {
			return true;
		}

		const selectedStatus = $('#project-status-filter').val();
		const selectedPriority = $('#project-priority-filter').val();

		const rowStatus = normalizeText(data[3]);
		const rowPriority = normalizeText(data[4]);

		if (selectedStatus && rowStatus !== selectedStatus) {
			return false;
		}

		if (selectedPriority && rowPriority !== selectedPriority) {
			return false;
		}

		return true;
	});

	// キーワード入力時に検索する
	$('#project-keyword-filter').on('keyup', function() {
		applyProjectSearch(table);
	});

	// Enterで送信されないようにする
	$('#project-keyword-filter').on('keydown', function(event) {
		if (event.key === 'Enter') {
			event.preventDefault();
			applyProjectSearch(table);
		}
	});

	// セレクト変更時に検索する
	$('#project-status-filter, #project-priority-filter').on('change', function() {
		applyProjectSearch(table);
	});

	// クリアボタンで検索条件を初期化する
	$('#project-clear-button').on('click', function() {

		$('#project-keyword-filter').val('');
		$('#project-status-filter').val('');
		$('#project-priority-filter').val('');

		table.search('');
		table.draw();

		updateProjectCounts(table);
	});

	// 描画後に件数を更新する
	table.on('draw', function() {
		updateProjectCounts(table);
	});

	// 初期件数を表示する
	updateProjectCounts(table);
});

/**
* 案件検索条件を反映する.
* @param {DataTables.Api} table DataTables.
*/
function applyProjectSearch(table) {

	const keyword = $('#project-keyword-filter').val();

	table.search(keyword);
	table.draw();

	updateProjectCounts(table);
}

/**
* 表示中案件の件数を更新する.
* @param {DataTables.Api} table DataTables.
*/
function updateProjectCounts(table) {

	const rows = table.rows({
		search: 'applied'
	}).data();

	let totalCount = 0;
	let progressCount = 0;
	let doneCount = 0;
	let stopCount = 0;

	rows.each(function(row) {

		const status = normalizeText(row[3]);

		totalCount++;

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

	$('#projectTotalCount').text(totalCount);
	$('#projectProgressCount').text(progressCount);
	$('#projectDoneCount').text(doneCount);
	$('#projectStopCount').text(stopCount);
}

/**

* HTML混じりの文字からテキストだけ取り出す.
* @param {string} value 変換前文字列.
* @return {string} 変換後文字列.
 */
function normalizeText(value) {

	if (value === null || value === undefined) {
		return '';
	}

	return $('<div>').html(value).text().trim();
}