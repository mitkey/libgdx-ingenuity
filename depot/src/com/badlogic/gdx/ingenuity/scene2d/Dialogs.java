package com.badlogic.gdx.ingenuity.scene2d;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.ingenuity.GdxData;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

/**
 * @作者 mitkey
 * @时间 2017年7月4日 下午3:43:43
 * @类说明 Dialogs.java <br/>
 * @版本 0.0.1
 */
public class Dialogs {

	private static final char[] charArray = FreeTypeFontGenerator.DEFAULT_CHARS.toCharArray();

	public static Dialog showInputDialog(Stage stage, String title, String fieldName, String fieldValue, Skin skin, InputDialogListener inputDialogListener) {
		return showInputDialog(stage, title, fieldName, fieldValue, skin, null, inputDialogListener);
	}

	public static Dialog showInputDialog(Stage stage, String title, String fieldName, String fieldValue, Skin skin, //
			final TextFieldFilter textFieldFilter, final InputDialogListener inputDialogListener) {
		final TextField textField = new TextField(fieldValue, skin);
		textField.setTextFieldFilter(textFieldFilter);
		textField.setMessageText("please input content");
		textField.setCursorPosition(fieldValue.length());

		Dialog dialog = new SimpleDialog(title, skin) {
			@Override
			protected void result(Object object) {
				super.result(object);
				if ((Boolean) object) {
					inputDialogListener.finished(textField.getText());
				}
			}
		};
		dialog.getContentTable().add(fieldName + ":", "title").padRight(10);
		dialog.getContentTable().add(textField);

		dialog.button("confirm", true).key(Keys.ENTER, true).button("cancel", false).key(Keys.ESCAPE, false);
		dialog.getButtonTable().pad(20);
		stage.setKeyboardFocus(textField);
		return dialog.show(stage);
	}

	public static Dialog showOkCancelDialog(Stage stage, String title, String text, Skin skin, final OkCancelDialogListener listener) {
		Dialog dialog = new SimpleDialog(title, skin) {
			@Override
			protected void result(Object object) {
				super.result(object);
				if ((Boolean) object) {
					listener.ok();
				}
			}
		};
		dialog.text(text).button("confirm", true).key(Keys.ENTER, true).button("cancel", false).key(Keys.ESCAPE, false);
		dialog.getButtonTable().pad(20);
		return dialog.show(stage);
	}

	public static Dialog showOkDialog(Stage stage, String title, String text, Skin skin) {
		Dialog dialog = new SimpleDialog(title, skin);
		dialog.text(text).button("confirm", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		dialog.getButtonTable().pad(20);
		return dialog.show(stage);
	}

	private static boolean checkHasChar(char c) {
		for (char temp : charArray) {
			if (temp == c) {
				return true;
			}
		}
		return false;
	}

	/** 过滤默认的字符 @see {@link FreeTypeFontGenerator} */
	public static class InputDialogDefaultCharsFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return checkHasChar(c);
		}
	}

	/** 文件名规范过滤 --> 不能出现中文和特色符号 */
	public static class InputDialogFileNameSpecificationFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			// 字母或数字
			return Character.isLetterOrDigit(c) || '_' == c;
		}
	}

	/** float 型过滤 --> 不能出现中文和特色符号 */
	public static class InputDialogFloatFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return Character.isDigit(c) || (!textField.getText().contains(".") && '.' == c);
		}
	}

	/** int 型字符过滤 --> 不能出现中文和特色符号 */
	public static class InputDialogIntegerFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return Character.isDigit(c);
		}
	}

	/** json 格式数据过滤 --> 不能出现中文和特色符号 */
	public static class InputDialogJsonFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return checkHasChar(c) || '_' == c || ':' == c || ',' == c //
					|| '[' == c || ']' == c || '\'' == c || '\"' == c;
		}
	}

	/** 字母、数字、 _ 过滤 --> 不能出现中文和特色符号 */
	public static class InputDialogLetterOrDigitFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return Character.isLetterOrDigit(c) || '_' == c;
		}
	}

	public interface InputDialogListener {
		void finished(String input);
	}

	public interface OkCancelDialogListener {
		void ok();
	}

	private static class SimpleDialog extends Dialog {
		public SimpleDialog(String title, Skin skin) {
			super(title, skin);
			getColor().a = 0;
			getButtonTable().defaults().pad(10).height(35).space(10).minWidth(120);
		}

		@Override
		public float getPrefHeight() {
			return GdxData.HEIGHT / 3;
		}

		@Override
		public float getPrefWidth() {
			return GdxData.WIDTH / 3;
		}
	}

}
