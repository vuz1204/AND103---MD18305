// Generated by view binder compiler. Do not edit!
package fpoly.vunvph33438.lab5.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import fpoly.vunvph33438.lab5.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityForgotPasswordBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnReset;

  @NonNull
  public final TextInputEditText edEmailForgotPass;

  private ActivityForgotPasswordBinding(@NonNull LinearLayout rootView, @NonNull Button btnReset,
      @NonNull TextInputEditText edEmailForgotPass) {
    this.rootView = rootView;
    this.btnReset = btnReset;
    this.edEmailForgotPass = edEmailForgotPass;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityForgotPasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityForgotPasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_forgot_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityForgotPasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnReset;
      Button btnReset = ViewBindings.findChildViewById(rootView, id);
      if (btnReset == null) {
        break missingId;
      }

      id = R.id.edEmailForgotPass;
      TextInputEditText edEmailForgotPass = ViewBindings.findChildViewById(rootView, id);
      if (edEmailForgotPass == null) {
        break missingId;
      }

      return new ActivityForgotPasswordBinding((LinearLayout) rootView, btnReset,
          edEmailForgotPass);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
