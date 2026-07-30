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

/**
 * 案件編集画面のjQuery処理.
 * 入力チェック、キャンセル、案件詳細遷移、工数表示を行う.
 */
jQuery(function ($) {

	// 案件編集フォームがない画面では処理しない
	if ($('#projectEditForm').length === 0) {
		return;
	}

	// date入力に入るように日付形式を補正する
	normalizeDateInput('#start_date');
	normalizeDateInput('#due_date');

	// 初期状態を保持する
	const initialFormValues = $('#projectEditForm').serializeArray();

	// 初期表示時に工数サマリーを更新する
	updateProjectEditSummary();

	// 見積工数が変わったらサマリーを更新する
	$('#estimated_manhours').on('input', function () {
		updateProjectEditSummary();
	});

	// 戻るボタン
	$('#back').on('click', function () {
		history.back();
	});

	// キャンセルボタン
	$('#clearBtn').on('click', function () {
		restoreProjectEditForm(initialFormValues);
		updateProjectEditSummary();
	});

	// 案件詳細へ戻る
	$('#move').on('click', function () {

		const projectId = $('#project_id').val();

		if (!projectId) {
			alert('案件IDが取得できません');
			return;
		}

		location.href = getContextPathForProject()
			+ '/Controller?page_id=P002&project_id='
			+ encodeURIComponent(projectId);
	});

	// 保存前入力チェック
	$('#projectEditForm').on('submit', function (event) {

		if (!validateProjectEditForm()) {
			event.preventDefault();
		}
	});
});

/**
 * 案件編集フォームを確認する.
 *
 * @return {boolean} 正常ならtrue.
 */
function validateProjectEditForm() {

	const projectName = $('#project_name').val();
	const projectManagerId = $('#project_manager_id').val();
	const startDate = $('#start_date').val();
	const dueDate = $('#due_date').val();
	const status = $('#status').val();
	const priority = $('#priority').val();
	const estimatedManhours = $('#estimated_manhours').val();

	if (isBlankProjectValue(projectName)) {
		alert('案件名を入力してください');
		return false;
	}

	if (isBlankProjectValue(projectManagerId)) {
		alert('担当PMを選択してください');
		return false;
	}

	if (isBlankProjectValue(startDate)) {
		alert('開始日を入力してください');
		return false;
	}

	if (isBlankProjectValue(dueDate)) {
		alert('期限を入力してください');
		return false;
	}

	if (startDate > dueDate) {
		alert('期限は開始日以降の日付を選択してください');
		return false;
	}

	if (isBlankProjectValue(status)) {
		alert('ステータスを選択してください');
		return false;
	}

	if (isBlankProjectValue(priority)) {
		alert('優先度を選択してください');
		return false;
	}

	if (isBlankProjectValue(estimatedManhours)) {
		alert('見積工数を入力してください');
		return false;
	}

	const manhoursValue = Number(estimatedManhours);

	if (Number.isNaN(manhoursValue)) {
		alert('見積工数は数値で入力してください');
		return false;
	}

	if (manhoursValue < 0) {
		alert('見積工数は0以上で入力してください');
		return false;
	}

	if (manhoursValue * 2 !== Math.floor(manhoursValue * 2)) {
		alert('見積工数は0.5単位で入力してください');
		return false;
	}

	return true;
}

/**
 * 案件編集サマリーを更新する.
 */
function updateProjectEditSummary() {

	const estimated = toNumberProjectValue($('#estimated_manhours').val());
	const actual = toNumberProjectValue($('#actual_manhours').val());
	const completedTaskCount = toNumberProjectValue($('#completed_task_count').val());
	const taskCount = toNumberProjectValue($('#task_count').val());
	const progressRate = toNumberProjectValue($('#progress_rate').val());

	let budgetRate = 0;

	if (estimated > 0) {
		budgetRate = Math.round((actual / estimated) * 100);
	}

	let taskProgressRate = progressRate;

	if (taskProgressRate <= 0 && taskCount > 0) {
		taskProgressRate = Math.round((completedTaskCount / taskCount) * 100);
	}

	$('#summaryEstimatedManhours').text(estimated);
	$('#summaryActualManhours').text(actual);
	$('#budgetRateText').text(budgetRate);
	$('#label-fraction').text(completedTaskCount + ' / ' + taskCount);
	$('#label-pct').text(taskProgressRate + '%');
	$('#js-bar').css('width', taskProgressRate + '%');
}

/**
 * フォームを初期値へ戻す.
 *
 * @param {Array} initialValues 初期値.
 */
function restoreProjectEditForm(initialValues) {

	initialValues.forEach(function (item) {
		const input = $('[name="' + item.name + '"]');

		if (input.length > 0) {
			input.val(item.value);
		}
	});

	normalizeDateInput('#start_date');
	normalizeDateInput('#due_date');
}

/**
 * date input用にyyyy-MM-ddへ補正する.
 *
 * @param {string} selector 対象セレクタ.
 */
function normalizeDateInput(selector) {

	const input = $(selector);

	if (input.length === 0) {
		return;
	}

	const value = input.val();

	if (!value) {
		return;
	}

	input.val(value.replace(/\//g, '-'));
}

/**
 * 数値へ変換する.
 *
 * @param {string} value 変換前文字列.
 * @return {number} 数値.
 */
function toNumberProjectValue(value) {

	if (isBlankProjectValue(value)) {
		return 0;
	}

	const numberValue = Number(value);

	if (Number.isNaN(numberValue)) {
		return 0;
	}

	return numberValue;
}

/**
 * 空文字か確認する.
 *
 * @param {string} value 確認値.
 * @return {boolean} 空ならtrue.
 */
function isBlankProjectValue(value) {
	return value === null || value === undefined || String(value).trim().length === 0;
}

/**
 * contextPathを取得する.
 *
 * @return {string} contextPath.
 */
function getContextPathForProject() {

	const pathParts = window.location.pathname.split('/');

	if (pathParts.length <= 1) {
		return '';
	}

	return '/' + pathParts[1];
}