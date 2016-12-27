package anylife.androiddbmanger.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import anylife.androiddbmanger.MyApplication;
import anylife.androiddbmanger.R;
import anylife.androiddbmanger.dbtest.NoteType;
import anylife.androiddbmanger.dbtest.NotesAdapter;
import anylife.androiddbmanger.entity.Note;
import anylife.androiddbmanger.entity.daoManger.DaoSession;
import anylife.androiddbmanger.entity.daoManger.NoteDao;

/**
 * 测试数据库的基本的操作
 *
 */
public class MainActivity extends AppCompatActivity {
	private EditText editText;
	private View addNoteButton;
	private NoteDao noteDao;
	private Query<Note> notesQuery;
	private NotesAdapter notesAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setUpViews();

		// get the note DAO
		DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();  //就是这样写，还搞什么单
		noteDao = daoSession.getNoteDao();

		// query all notes, sorted a-z by their text
		notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();
		updateNotes();

		new AlertDialog.Builder(MainActivity.this)
				.setTitle("提示")
				.setMessage("是否进入本地缓存消息列表后长时间没有更新数据的场景")
				.setCancelable(true)
				.setPositiveButton("是的", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//去进行场景的.
					}
				})
				.setNegativeButton("下次吧", null).show();
	}

	/**
	 * 查询更新所有的数据
	 */
	private void updateNotes() {
		List<Note> notes = notesQuery.list();
		notesAdapter.setNotes(notes);
	}

	/**
	 *
	 */
	protected void setUpViews() {
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
		//noinspection ConstantConditions
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		notesAdapter = new NotesAdapter(noteClickListener);
		recyclerView.setAdapter(notesAdapter);

		addNoteButton = findViewById(R.id.buttonAdd);
		//noinspection ConstantConditions
		addNoteButton.setEnabled(false);

		editText = (EditText) findViewById(R.id.editTextNote);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					addNote();
					return true;
				}
				return false;
			}
		});
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				boolean enable = s.length() != 0;
				addNoteButton.setEnabled(enable);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	public void onAddButtonClick(View view) {
		addNote();
	}

	/**
	 * 插入一条新的数据
	 */
	private void addNote() {
		String noteText = editText.getText().toString();
		editText.setText("");
		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		String comment = "Added on " + df.format(new Date());

		Note note = new Note(null, noteText, comment, new Date(), NoteType.TEXT);
		noteDao.insert(note);
		Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

		updateNotes();
	}

	/**
	 * 通过主键删除数据
	 */
	NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
		@Override
		public void onNoteClick(int position) {
			Note note = notesAdapter.getNote(position);
			Long noteId = note.getId();
			noteDao.deleteByKey(noteId);
			Log.d("DaoExample", "Deleted note, ID: " + noteId);
			updateNotes();
		}
	};


}