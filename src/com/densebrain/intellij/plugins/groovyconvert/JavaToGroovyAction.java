package com.densebrain.intellij.plugins.groovyconvert;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.log4j.Logger;

/**
 * Created by jglanz on 12/5/14.
 */
public class JavaToGroovyAction extends AnAction {
	private static final Logger logger = Logger.getLogger(JavaToGroovyAction.class);

	public void update(AnActionEvent e) {
		super.update(e);


		VirtualFile f = e.getData(PlatformDataKeys.VIRTUAL_FILE);
		//Object s = e.getData(PlatformDataKeys.SELECTED_ITEM);


		boolean visible = f != null && f.getFileType().equals(JavaFileType.INSTANCE);

		e.getPresentation().setEnabledAndVisible(visible);

	}


	public void actionPerformed(AnActionEvent e) {
		final Project project = e.getProject();
		final VirtualFile f = e.getData(PlatformDataKeys.VIRTUAL_FILE);

		if (f == null || !f.getFileType().equals(JavaFileType.INSTANCE)) {
			return;
		}

		Module module = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(f);
		ModuleRootManager mrm = ModuleRootManager.getInstance(module);

		VirtualFile[] sources = mrm.getSourceRoots();
		VirtualFile groot = null;
		for (VirtualFile source : sources) {
			logger.debug(source.toString());

			if (source.getName().equals("groovy"))
				groot = source;
		}

		ApplicationManager.getApplication().runWriteAction(new Runnable() {
			@Override
			public void run() {
				try {
					f.rename(this, f.getNameWithoutExtension() + ".groovy");

				} catch (Exception ex) {
					logger.error("Failed to convert to groovy", ex);
				}
			}
		});





	}
}
