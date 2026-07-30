'use strict';

/**
 * 共通JS.
 * 確認ダイアログ、ログアウト確認、パスワード変更モーダルを制御する.
 */

/**
 * 登録確認を表示する.
 * JSPのonsubmitから呼び出される想定.
 * @return {boolean} OKならtrue.
 */
function registMessage() {
	return window.confirm('この内容で登録しますか？');
}

/**
 * 削除確認を表示する.
 * JSPのonsubmitから呼び出される想定.
 * @return {boolean} OKならtrue.
 */
function deleteMessage() {
	return window.confirm('削除しますか？');
}

/**
 * 完了確認を表示する.
 * JSPのonsubmitから呼び出される想定.
 * @return {boolean} OKならtrue.
 */
function completeMessage() {
	return window.confirm('この内容で登録しますか？');
}

/**
 * 中止確認を表示する.
 * JSPのonsubmitから呼び出される想定.
 * @return {boolean} OKならtrue.
 */
function stopMessage() {
	return window.confirm('中止しますか？');
}

/**
 * HTML読み込み後に共通処理を開始する.
 */
document.addEventListener('DOMContentLoaded', function () {

	// 共通処理の二重実行を防ぐ
	if (document.body && document.body.dataset.commonJsInitialized === 'true') {
		return;
	}

	// 初期化済みフラグを立てる
	if (document.body) {
		document.body.dataset.commonJsInitialized = 'true';
	}

	// data-confirm付きフォームを初期化する
	initConfirmForm();

	// パスワード変更モーダルを初期化する
	initPasswordChangeModal();
});

/**
 * data-confirm付きフォームの確認処理を設定する.
 */
function initConfirmForm() {

	// data-confirm付きフォームを取得する
	const confirmForms = document.querySelectorAll('form[data-confirm]');

	// 各フォームに送信前確認を設定する
	confirmForms.forEach(function (form) {

		// 同じフォームへ二重設定しない
		if (form.dataset.confirmInitialized === 'true') {
			return;
		}

		// 初期化済みフラグを立てる
		form.dataset.confirmInitialized = 'true';

		// 送信前に確認する
		form.addEventListener('submit', function (event) {

			const message = form.dataset.confirm;

			if (message && !window.confirm(message)) {
				event.preventDefault();
			}
		});
	});
}

/**
 * パスワード変更モーダルを初期化する.
 */
function initPasswordChangeModal() {

	// モーダルを取得する
	const modal = document.getElementById('passwordChangeModal');

	// モーダルがない画面では処理しない
	if (!modal) {
		return;
	}

	// 二重設定を防ぐ
	if (modal.dataset.passwordModalInitialized === 'true') {
		return;
	}

	// 初期化済みフラグを立てる
	modal.dataset.passwordModalInitialized = 'true';

	// 操作用部品を取得する
	const openButton = document.getElementById('openPasswordModalButton');
	const closeButton = document.getElementById('closePasswordModalButton');
	const cancelButton = document.getElementById('cancelPasswordModalButton');
	const form = document.getElementById('passwordChangeForm');
	const initialOpen = document.getElementById('passwordModalInitialOpen');

	// 初期状態では必ず閉じる
	modal.classList.remove('show');

	// パスワード変更ボタンでモーダルを開く
	if (openButton) {
		openButton.addEventListener('click', function (event) {
			event.preventDefault();
			openPasswordChangeModal();
		});
	}

	// 閉じるボタンでモーダルを閉じる
	if (closeButton) {
		closeButton.addEventListener('click', function () {
			closePasswordChangeModal();
		});
	}

	// キャンセルボタンでモーダルを閉じる
	if (cancelButton) {
		cancelButton.addEventListener('click', function () {
			closePasswordChangeModal();
		});
	}

	// 背景クリックでモーダルを閉じる
	modal.addEventListener('click', function (event) {
		if (event.target === modal) {
			closePasswordChangeModal();
		}
	});

	// Escキーでモーダルを閉じる
	document.addEventListener('keydown', function (event) {
		if (event.key === 'Escape' && modal.classList.contains('show')) {
			closePasswordChangeModal();
		}
	});

	// 送信前に入力チェックを行う
	if (form) {
		form.addEventListener('submit', function (event) {
			if (!validatePasswordChangeForm()) {
				event.preventDefault();
			}
		});
	}

	// サーバー側で再表示指定がある場合は開く
	if (initialOpen) {
		openPasswordChangeModal();
	}
}

/**
 * パスワード変更モーダルを開く.
 */
function openPasswordChangeModal() {

	// モーダルを取得する
	const modal = document.getElementById('passwordChangeModal');

	// 最初の入力欄を取得する
	const currentPassword = document.getElementById('current_password');

	// モーダルがない場合は処理しない
	if (!modal) {
		return;
	}

	// モーダルを表示する
	modal.classList.add('show');

	// 背面スクロールを止める
	document.body.classList.add('modal-open');

	// 現在パスワードへフォーカスする
	if (currentPassword) {
		currentPassword.focus();
	}
}

/**
 * パスワード変更モーダルを閉じる.
 */
function closePasswordChangeModal() {

	// モーダルを取得する
	const modal = document.getElementById('passwordChangeModal');

	// フォームを取得する
	const form = document.getElementById('passwordChangeForm');

	// モーダルがない場合は処理しない
	if (!modal) {
		return;
	}

	// モーダルを閉じる
	modal.classList.remove('show');

	// 背面スクロールを戻す
	document.body.classList.remove('modal-open');

	// 入力値を消す
	if (form) {
		form.reset();
	}
}

/**
 * パスワード変更フォームを確認する.
 * @return {boolean} 正常ならtrue.
 */
function validatePasswordChangeForm() {

	// 入力値を取得する
	const currentPassword = getPasswordInputValue('current_password');
	const newPassword = getPasswordInputValue('new_password');
	const confirmPassword = getPasswordInputValue('confirm_password');

	// 現在パスワードを確認する
	if (!currentPassword) {
		window.alert('現在のパスワードを入力してください');
		return false;
	}

	// 新しいパスワードを確認する
	if (!newPassword) {
		window.alert('新しいパスワードを入力してください');
		return false;
	}

	// 確認用パスワードを確認する
	if (!confirmPassword) {
		window.alert('新しいパスワード確認を入力してください');
		return false;
	}

	// 新しいパスワードの文字数を確認する
	if (newPassword.length < 6) {
		window.alert('新しいパスワードは6文字以上で入力してください');
		return false;
	}

	// 新しいパスワードと確認用パスワードの一致を確認する
	if (newPassword !== confirmPassword) {
		window.alert('新しいパスワードと確認用パスワードが一致しません');
		return false;
	}

	// 現在パスワードと同じ値になっていないか確認する
	if (currentPassword === newPassword) {
		window.alert('現在のパスワードとは別の値を入力してください');
		return false;
	}

	return true;
}

/**
 * パスワード入力欄の値を取得する.
 * @param {string} id inputのID.
 * @return {string} 入力値.
 */
function getPasswordInputValue(id) {

	// 入力欄を取得する
	const input = document.getElementById(id);

	// 入力欄がない場合は空文字を返す
	if (!input) {
		return '';
	}

	return input.value.trim();
}

/**
 * inputの値を取得する.
 * 既存コード互換用.
 * @param {string} id inputのID.
 * @return {string} 入力値.
 */
function getInputValue(id) {
	return getPasswordInputValue(id);
}